package dev.hieplp.library.service.impl;

import dev.hieplp.library.common.entity.Password;
import dev.hieplp.library.common.entity.TempUser;
import dev.hieplp.library.common.entity.User;
import dev.hieplp.library.common.entity.UserRole;
import dev.hieplp.library.common.entity.key.UserRoleKey;
import dev.hieplp.library.common.enums.IdLength;
import dev.hieplp.library.common.enums.otp.OtpStatus;
import dev.hieplp.library.common.enums.otp.OtpType;
import dev.hieplp.library.common.enums.token.TokenType;
import dev.hieplp.library.common.enums.user.Role;
import dev.hieplp.library.common.enums.user.TempUserStatus;
import dev.hieplp.library.common.enums.user.UserStatus;
import dev.hieplp.library.common.exception.BadRequestException;
import dev.hieplp.library.common.exception.NotFoundException;
import dev.hieplp.library.common.exception.UnauthorizedException;
import dev.hieplp.library.common.exception.user.InvalidUserNameOrPasswordException;
import dev.hieplp.library.common.exception.user.NotVerifiedException;
import dev.hieplp.library.common.helper.OtpHelper;
import dev.hieplp.library.common.helper.RoleHelper;
import dev.hieplp.library.common.helper.UserHelper;
import dev.hieplp.library.common.model.TokenModel;
import dev.hieplp.library.common.util.*;
import dev.hieplp.library.config.AppConfig;
import dev.hieplp.library.config.security.CurrentUser;
import dev.hieplp.library.payload.request.auth.LoginRequest;
import dev.hieplp.library.payload.request.auth.RefreshAccessTokenRequest;
import dev.hieplp.library.payload.request.auth.UpdateRootPasswordRequest;
import dev.hieplp.library.payload.request.auth.register.ConfirmRegisterRequest;
import dev.hieplp.library.payload.request.auth.register.RequestToRegisterRequest;
import dev.hieplp.library.payload.request.auth.register.ResendRegisterOtpRequest;
import dev.hieplp.library.payload.request.auth.verify.ConfirmVerifyRequest;
import dev.hieplp.library.payload.request.auth.verify.RequestToVerifyRequest;
import dev.hieplp.library.payload.request.auth.verify.ResendVerifyOtpRequest;
import dev.hieplp.library.payload.response.auth.LoginResponse;
import dev.hieplp.library.payload.response.auth.register.ConfirmRegisterResponse;
import dev.hieplp.library.payload.response.auth.register.RequestToRegisterResponse;
import dev.hieplp.library.payload.response.auth.register.ResendRegisterOtpResponse;
import dev.hieplp.library.payload.response.auth.verify.ConfirmVerifyResponse;
import dev.hieplp.library.payload.response.auth.verify.RequestToVerifyResponse;
import dev.hieplp.library.payload.response.auth.verify.ResendVerifyOtpResponse;
import dev.hieplp.library.repository.OtpRepository;
import dev.hieplp.library.repository.PasswordRepository;
import dev.hieplp.library.repository.TempUserRepository;
import dev.hieplp.library.repository.UserRepository;
import dev.hieplp.library.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.PrivateKey;
import java.util.HashSet;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final String ROOT_USERID = "root";
    private final String ROOT_USERNAME = "root";

    private final CurrentUser currentUser;

    private final UserRepository userRepo;
    private final PasswordRepository passwordRepo;
    private final TempUserRepository tempUserRepo;
    private final OtpRepository otpRepo;

    private final OtpHelper otpHelper;
    private final UserHelper userHelper;
    private final RoleHelper roleHelper;

    private final MaskUtil maskUtil;
    private final EmailUtil emailUtil;
    private final TokenUtil tokenUtil;
    private final EncryptUtil encryptUtil;
    private final DateTimeUtil dateTimeUtil;
    private final GeneratorUtil generatorUtil;

    private final AppConfig appConfig;
    private final PrivateKey tokenPrivateKey;
    private final PrivateKey passwordPrivateKey;
    private final JavaMailSender javaMailSender;

    @Override
    public RequestToRegisterResponse requestToRegister(RequestToRegisterRequest request) {
        log.info("Request to register with request: {}", request);

        // Check duplicate email and username
        userHelper.validateEmail(request.getEmail());
        userHelper.validateUsername(request.getUsername());

        // Check quota for today
        otpHelper.validateOtpQuota(request.getEmail(), OtpType.REGISTER);

        // Save otp
        var otp = otpHelper.initOtp(OtpType.REGISTER, request.getEmail());
        otpRepo.save(otp);

        // Save temporary user
        var salt = generatorUtil.generateSalt();
        var password = encryptUtil.generatePassword(request.getPassword(), passwordPrivateKey, salt);
        var userId = generatorUtil.generateId(IdLength.USER_ID);
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
        emailUtil.sendMime(javaMailSender, request.getEmail(), "Confirm register OTP", otp.getToken());

        // Return response
        return RequestToRegisterResponse.builder()
                .otpId(otp.getOtpId())
                .maskedEmail(maskUtil.maskEmail(request.getEmail()))
                .expiryTime(otp.getExpiryTime())
                .issuedTime(otp.getIssueTime())
                .build();
    }

    @Override
    public ResendRegisterOtpResponse resendRegisterOtp(ResendRegisterOtpRequest request) {
        log.info("Resend register otp with request: {}", request);

        var otp = otpHelper.getOtp(request.getOtpId());

        var otpConfig = otpHelper.getOtpConfig(OtpType.REGISTER);

        // Check resend quota for today
        otpHelper.validateResendOtpQuota(otp, otpConfig);

        // Increase resend count
        otp
                .setResendCount(otp.getResendCount() + 1)
                .setModifiedAt(dateTimeUtil.getCurrentTimestamp());
        otpRepo.save(otp);

        // Resend email
        emailUtil.sendMime(javaMailSender, otp.getSendTo(), "Resend Register OTP", otp.getToken());

        // Return response
        return ResendRegisterOtpResponse.builder()
                .otpId(otp.getOtpId())
                .maskedEmail(maskUtil.maskEmail(otp.getSendTo()))
                .expiryTime(otp.getExpiryTime())
                .issuedTime(otp.getIssueTime())
                .resendCount(otp.getResendCount())
                .resendQuota(otpConfig.getResendQuota())
                .build();
    }

    @Override
    public ConfirmRegisterResponse confirmRegister(ConfirmRegisterRequest request) {
        log.info("Confirm register with request: {}", request);

        var otp = otpHelper.getOtpByTokenAndType(request.getToken(), OtpType.REGISTER);

        // Validate OTP life time
        otpHelper.validateOtpLifeTime(otp);

        // Check duplicate email and username
        var tempUser = tempUserRepo.findByOtp(otp)
                .orElseThrow(() -> {
                    log.warn("Temp user with otp id {} not found", otp.getOtpId());
                    return new NotFoundException(String.format("Temp user with otp id %s not found", otp.getOtpId()));
                });

        userHelper.validateEmail(tempUser.getEmail());
        userHelper.validateUsername(tempUser.getUsername());

        // Update OTP status
        otp
                .setStatus(OtpStatus.USED.getStatus())
                .setModifiedAt(dateTimeUtil.getCurrentTimestamp());
        otpRepo.save(otp);

        // Save user information
        var userId = generatorUtil.generateId(IdLength.USER_ID);

        var roles = new HashSet<UserRole>();
        roles.add(UserRole.builder()
                .id(UserRoleKey.builder()
                        .userId(userId)
                        .role(Role.USER.getRole())
                        .build())
                .build());

        var user = User.builder()
                .userId(userId)
                .username(tempUser.getUsername())
                .email(tempUser.getEmail())
                .status(UserStatus.ACTIVE.getStatus())
                .roles(roles)
                .createdBy(userId)
                .createdAt(dateTimeUtil.getCurrentTimestamp())
                .modifiedBy(userId)
                .modifiedAt(dateTimeUtil.getCurrentTimestamp())
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
                .maskedEmail(maskUtil.maskEmail(tempUser.getEmail()))
                .build();
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        log.info("Login with request: {}", request);

        var user = userRepo.findByUsername(request.getUsername())
                .orElseThrow(() -> {
                    log.warn("User with username {} not found", request.getUsername());
                    return new InvalidUserNameOrPasswordException();
                });

        if (UserStatus.INACTIVE.getStatus().equals(user.getStatus())) {
            var message = String.format("User %s is inactive", user.getUserId());
            log.warn(message);
            throw new UnauthorizedException(message);
        }

        if (UserStatus.NOT_VERIFIED.getStatus().equals(user.getStatus())) {
            var message = String.format("User %s not verified", user.getUserId());
            log.warn(message);
            throw new NotVerifiedException(message);
        }

        var password = passwordRepo.findById(user.getUserId())
                .orElseThrow(() -> {
                    log.warn("Password for user {} not found", user.getUserId());
                    return new InvalidUserNameOrPasswordException();
                });

        // Validate password
        var isPassMatched = encryptUtil.validatePassword(request.getPassword(), password.getPassword(), password.getSalt(), passwordPrivateKey);
        if (!isPassMatched) {
            log.warn("Password not matched");
            throw new InvalidUserNameOrPasswordException();
        }

        //  roles
        var roles = roleHelper.getRolesByUserId(user.getUserId());

        // Generate accessToken and refreshToken
        var accessToken = tokenUtil.generateToken(appConfig.getAccessToken(), tokenPrivateKey, TokenType.ACCESS_TOKEN, user, roles);
        var refreshToken = tokenUtil.generateToken(appConfig.getRefreshToken(), tokenPrivateKey, TokenType.REFRESH_TOKEN, user);

        // Return response
        return LoginResponse.builder()
                .user(user)
                .roles(roles)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public TokenModel refreshAccessToken(RefreshAccessTokenRequest request) {
        log.info("Refresh access token with request: {}", request);

        if (!TokenType.REFRESH_TOKEN.getType().equals(currentUser.getTokenType())) {
            log.warn("Current token is not refresh token");
            throw new UnauthorizedException("Current token is not refresh token");
        }

        var user = userRepo.findById(currentUser.getUserId())
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        var roles = roleHelper.getRolesByUserId(user.getUserId());

        return tokenUtil.generateToken(appConfig.getAccessToken(), tokenPrivateKey, TokenType.ACCESS_TOKEN, user, roles);

    }

    @Override
    public RequestToVerifyResponse requestToVerify(RequestToVerifyRequest request) {
        log.info("Request to verify: {}", request);

        var user = userRepo.findByUsernameAndEmail(request.getUsername(), request.getEmail())
                .orElseThrow(() -> {
                    var message = String.format("User with username %s and email %s not found", request.getUsername(), request.getEmail());
                    log.warn(message);
                    return new NotFoundException(message);
                });

        if (!UserStatus.NOT_VERIFIED.getStatus().equals(user.getStatus())) {
            var message = String.format("User %s is not not_verified", user.getUserId());
            log.warn(message);
            throw new BadRequestException(message);
        }

        // Check quota for today
        otpHelper.validateOtpQuota(user.getEmail(), OtpType.VERIFY);

        // Save otp
        var otp = otpHelper.initOtp(OtpType.VERIFY, user.getEmail());
        otpRepo.save(otp);

        // Send email
        emailUtil.sendMime(javaMailSender, user.getEmail(), "Confirm verify OTP", otp.getToken());

        // Return response
        return RequestToVerifyResponse.builder()
                .otpId(otp.getOtpId())
                .maskedEmail(maskUtil.maskEmail(request.getEmail()))
                .expiryTime(otp.getExpiryTime())
                .issuedTime(otp.getIssueTime())
                .build();
    }

    @Override
    public ResendVerifyOtpResponse resendVerifyOtp(ResendVerifyOtpRequest request) {
        log.info("Resend verify otp with request: {}", request);

        var otp = otpHelper.getOtp(request.getOtpId());
        var otpConfig = otpHelper.getOtpConfig(OtpType.VERIFY);

        // Check resend quota for today
        otpHelper.validateResendOtpQuota(otp, otpConfig);

        // Increase resend count
        otp
                .setResendCount(otp.getResendCount() + 1)
                .setModifiedAt(dateTimeUtil.getCurrentTimestamp());
        otpRepo.save(otp);

        // Resend email
        emailUtil.sendMime(javaMailSender, otp.getSendTo(), "Resend Verify OTP", otp.getToken());

        // Return response
        return ResendVerifyOtpResponse.builder()
                .otpId(otp.getOtpId())
                .maskedEmail(maskUtil.maskEmail(otp.getSendTo()))
                .expiryTime(otp.getExpiryTime())
                .issuedTime(otp.getIssueTime())
                .resendCount(otp.getResendCount())
                .resendQuota(otpConfig.getResendQuota())
                .build();
    }

    @Override
    public ConfirmVerifyResponse confirmVerify(ConfirmVerifyRequest request) {
        log.info("Confirm verify with request: {}", request);

        var otp = otpHelper.getOtpByTokenAndType(request.getToken(), OtpType.VERIFY);

        // Validate OTP life time
        otpHelper.validateOtpLifeTime(otp);

        //
        var user = userRepo.findByEmail(otp.getSendTo())
                .orElseThrow(() -> {
                    var message = String.format("User with email %s not found", otp.getSendTo());
                    log.warn(message);
                    return new NotFoundException(message);
                });

        if (!UserStatus.NOT_VERIFIED.getStatus().equals(user.getStatus())) {
            var message = String.format("User %s is not not verified", user.getUserId());
            log.warn(message);
            throw new BadRequestException(message);
        }

        // Update OTP status
        otp
                .setStatus(OtpStatus.USED.getStatus())
                .setModifiedAt(dateTimeUtil.getCurrentTimestamp());
        otpRepo.save(otp);

        // Verify user
        user
                .setStatus(UserStatus.ACTIVE.getStatus())
                .setModifiedBy(user.getUserId())
                .setModifiedAt(dateTimeUtil.getCurrentTimestamp());

        // Update password
        var salt = generatorUtil.generateSalt();
        var password = encryptUtil.generatePassword(request.getPassword(), passwordPrivateKey, salt);
        var passwordEntity = Password.builder()
                .userId(user.getUserId())
                .password(password)
                .salt(salt)
                .user(user)
                .build();

        passwordRepo.save(passwordEntity);

        // Return response
        return ConfirmVerifyResponse.builder()
                .maskedEmail(maskUtil.maskEmail(user.getEmail()))
                .build();
    }

    @Override
    public void updateRootPassword(UpdateRootPasswordRequest request) {
        log.info("Create root account with request: {}", request);

        var user = userHelper.getUser(ROOT_USERID);

        //  Check if token is root token
        if (!user.getRootToken().equals(request.getToken())) {
            var message = String.format("Token %s is not valid", request.getToken());
            log.warn(message);
            throw new UnauthorizedException(message);
        }

        // Update user
        user
                .setRootToken(generatorUtil.generateToken()) // Generate new root token
                .setModifiedBy(user.getUserId())
                .setModifiedAt(dateTimeUtil.getCurrentTimestamp());

        // Update password
        var salt = generatorUtil.generateSalt();
        var password = encryptUtil.generatePassword(request.getPassword(), passwordPrivateKey, salt);
        var passwordEntity = Password.builder()
                .userId(user.getUserId())
                .password(password)
                .salt(salt)
                .user(user)
                .build();

        passwordRepo.save(passwordEntity);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void createRootAccountAfterStartup() {
        log.info("Create root account after startup");

        final var ROOT_PASSWORD = generatorUtil.generateToken();

        // Check if root account existed
        if (userRepo.existsByUsername(ROOT_USERNAME)) {
            log.warn("Root account existed");
            return;
        }

        var roles = new HashSet<UserRole>();
        roles.add(UserRole.builder()
                .id(UserRoleKey.builder()
                        .userId(ROOT_USERID)
                        .role(Role.ROOT.getRole())
                        .build())
                .build());

        var user = User.builder()
                .userId(ROOT_USERID)
                .username(ROOT_USERNAME)
                .createdBy(ROOT_USERID)
                .status(UserStatus.ACTIVE.getStatus())
                .roles(roles)
                .rootToken(generatorUtil.generateToken())
                .createdAt(dateTimeUtil.getCurrentTimestamp())
                .modifiedBy(ROOT_USERID)
                .modifiedAt(dateTimeUtil.getCurrentTimestamp())
                .build();

        var salt = generatorUtil.generateSalt();
        var password = encryptUtil.generatePassword(ROOT_PASSWORD, salt);
        var passwordEntity = Password.builder()
                .userId(ROOT_USERID)
                .password(password)
                .salt(salt)
                .user(user)
                .build();

        passwordRepo.save(passwordEntity);
    }
}
