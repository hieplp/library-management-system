package dev.hieplp.library.repository;

import dev.hieplp.library.common.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpRepository extends JpaRepository<Otp, String> {

    @Query("SELECT COUNT(o) FROM Otp o WHERE o.sendTo = ?1 AND o.type = ?2 AND DATE(o.createdAt) = CURRENT_DATE")
    int countTodayOtpBySendToAndType(String sendTo, byte type);

}
