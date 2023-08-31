package dev.hieplp.library.service.impl;

import dev.hieplp.library.common.entity.Otp;
import dev.hieplp.library.common.entity.Password;
import dev.hieplp.library.common.entity.TempUser;
import dev.hieplp.library.common.entity.User;
import dev.hieplp.library.common.enums.IdLength;
import dev.hieplp.library.common.enums.otp.OtpStatus;
import dev.hieplp.library.common.enums.otp.OtpType;
import dev.hieplp.library.common.enums.user.TempUserStatus;
import dev.hieplp.library.common.enums.user.UserStatus;
import dev.hieplp.library.common.exception.NotFoundException;
import dev.hieplp.library.common.helper.OtpHelper;
import dev.hieplp.library.common.helper.UserHelper;
import dev.hieplp.library.common.util.*;
import dev.hieplp.library.payload.request.auth.LoginRequest;
import dev.hieplp.library.payload.request.auth.RefreshAccessTokenRequest;
import dev.hieplp.library.payload.request.auth.register.ConfirmRegisterRequest;
import dev.hieplp.library.payload.request.auth.register.RequestToRegisterRequest;
import dev.hieplp.library.payload.request.auth.register.ResendRegisterOtpRequest;
import dev.hieplp.library.payload.response.auth.LoginResponse;
import dev.hieplp.library.payload.response.auth.RefreshAccessTokenResponse;
import dev.hieplp.library.payload.response.auth.register.ConfirmRegisterResponse;
import dev.hieplp.library.payload.response.auth.register.RequestToRegisterResponse;
import dev.hieplp.library.payload.response.auth.register.ResendRegisterOtpResponse;
import dev.hieplp.library.repository.OtpRepository;
import dev.hieplp.library.repository.PasswordRepository;
import dev.hieplp.library.repository.TempUserRepository;
import dev.hieplp.library.repository.UserRepository;
import dev.hieplp.library.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.PrivateKey;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepo;
    private final PasswordRepository passwordRepo;
    private final TempUserRepository tempUserRepo;
    private final OtpRepository otpRepo;

    private final OtpHelper otpHelper;
    private final UserHelper userHelper;

    private final PrivateKey passwordPrivateKey;
    private final JavaMailSender javaMailSender;

    @Override
    @Transactional
    public RequestToRegisterResponse requestToRegister(RequestToRegisterRequest request) {
        log.info("Request to register with request: {}", request);

        // Check duplicate email and username
        userHelper.validateEmail(request.getEmail());
        userHelper.validateUsername(request.getUsername());

        // Check quota for today
        otpHelper.validateOtpQuota(request.getEmail(), OtpType.REGISTER);

        // Save otp
        var otpId = GeneratorUtil.generateId(IdLength.OTP_ID);
        var token = GeneratorUtil.generateToken();
        var otpConfig = otpHelper.getOtpConfig(OtpType.REGISTER);
        var currentTime = DateTimeUtil.getCurrentTimestamp();
        var expiryTime = DateTimeUtil.addSeconds(currentTime, otpConfig.getExpirationTime());
        var otp = Otp.builder()
                .otpId(otpId)
                .type(OtpType.REGISTER.getType())
                .sendTo(request.getEmail())
                .token(token)
                .issueTime(currentTime)
                .expiryTime(expiryTime)
                .resendCount(0)
                .createdAt(currentTime)
                .modifiedAt(currentTime)
                .status(OtpStatus.PENDING.getStatus())
                .build();

        // Save temporary user
        var salt = GeneratorUtil.generateSalt();
        var password = EncryptUtil.generatePassword(request.getPassword(), passwordPrivateKey, salt);
        var userId = GeneratorUtil.generateId(IdLength.USER_ID);
        var tempUser = TempUser.builder()
                .userId(userId)
                .username(request.getUsername())
                .email(request.getEmail())
                .password(password)
                .salt(salt)
                .otp(otp)
                .status(TempUserStatus.PENDING.getStatus())
                .build();
        tempUserRepo.save(tempUser);

        // Send email
        EmailUtil.sendMime(javaMailSender, request.getEmail(), "Confirm register OTP", token);

        // Return response
        return RequestToRegisterResponse.builder()
                .otpId(otpId)
                .maskedEmail(MaskUtil.maskEmail(request.getEmail()))
                .expiryTime(expiryTime)
                .issuedTime(currentTime)
                .build();
    }

    @Override
    @Transactional
    public ResendRegisterOtpResponse sendRegisterOtp(ResendRegisterOtpRequest request) {
        log.info("Resend register otp with request: {}", request);

        var otp = otpRepo.findById(request.getOtpId())
                .orElseThrow(() -> new NotFoundException(String.format("Otp id %s not found", request.getOtpId())));

        var otpConfig = otpHelper.getOtpConfig(OtpType.REGISTER);

        // Check resend quota for today
        otpHelper.validateResendOtpQuota(otp, otpConfig);

        // Increase resend count
        otp
                .setResendCount(otp.getResendCount() + 1)
                .setModifiedAt(DateTimeUtil.getCurrentTimestamp());
        otpRepo.save(otp);

        // Resend email
        EmailUtil.sendMime(javaMailSender, otp.getSendTo(), "Resend Register OTP", otp.getToken());

        // Return response
        return ResendRegisterOtpResponse.builder()
                .otpId(otp.getOtpId())
                .maskedEmail(MaskUtil.maskEmail(otp.getSendTo()))
                .expiryTime(otp.getExpiryTime())
                .issuedTime(otp.getIssueTime())
                .resendCount(otp.getResendCount())
                .resendQuota(otpConfig.getResendQuota())
                .build();
    }

    @Override
    @Transactional
    public ConfirmRegisterResponse confirmRegister(ConfirmRegisterRequest request) {
        log.info("Confirm register with request: {}", request);

        var otp = otpRepo.findByTokenAndType(request.getToken(), OtpType.REGISTER.getType())
                .orElseThrow(() -> new NotFoundException(String.format("Otp with token %s not found", request.getToken())));

        // Validate OTP life time
        otpHelper.validateOtpLifeTime(otp);

        // Check duplicate email and username
        var tempUser = tempUserRepo.findByOtp(otp)
                .orElseThrow(() -> new NotFoundException(String.format("Temp user with otp id %s not found", otp.getOtpId())));

        userHelper.validateEmail(tempUser.getEmail());
        userHelper.validateUsername(tempUser.getUsername());

        // Update OTP status
        otp
                .setStatus(OtpStatus.USED.getStatus())
                .setModifiedAt(DateTimeUtil.getCurrentTimestamp());
        otpRepo.save(otp);

        // Save user information
        var userId = GeneratorUtil.generateId(IdLength.USER_ID);

        var user = User.builder()
                .userId(userId)
                .username(tempUser.getUsername())
                .email(tempUser.getEmail())
                .status(UserStatus.ACTIVE.getStatus())
                .createdBy(userId)
                .createdAt(DateTimeUtil.getCurrentTimestamp())
                .modifiedBy(userId)
                .modifiedAt(DateTimeUtil.getCurrentTimestamp())
                .build();

        var password = Password.builder()
                .userId(userId)
                .password(tempUser.getPassword())
                .salt(tempUser.getSalt())
                .user(user)
                .build();

        passwordRepo.save(password);

        // Return response
        return ConfirmRegisterResponse.builder()
                .maskedEmail(MaskUtil.maskEmail(tempUser.getEmail()))
                .build();
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        log.info("Login with request: {}", request);

        var user = userRepo.findByUsername(request.getUsername())
                .orElseThrow(() -> new NotFoundException(String.format("User with username %s not found", request.getUsername())));

        var password = passwordRepo.findById(user.getUserId());
//
        log.error("Password: {}", password);

        return LoginResponse.builder()
                .user(user)
                .build();
    }

    @Override
    public RefreshAccessTokenResponse refreshAccessToken(RefreshAccessTokenRequest request) {
        log.info("Refresh access token with request: {}", request);
        return null;
    }
}
