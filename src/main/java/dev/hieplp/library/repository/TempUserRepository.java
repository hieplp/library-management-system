package dev.hieplp.library.repository;

import dev.hieplp.library.common.entity.Otp;
import dev.hieplp.library.common.entity.TempUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TempUserRepository extends JpaRepository<TempUser, String> {
    Optional<TempUser> findByOtp(Otp otp);
}
