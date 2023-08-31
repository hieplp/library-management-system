package dev.hieplp.library.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Otp {
    @Id
    private String otpId;

    @Column(length = 1)
    private Byte type;

    private String sendTo;

    private String token;

    private Timestamp issueTime;

    private Timestamp expiryTime;

    private Integer resendCount;

    @Column(length = 1)
    private Byte status;

    private Timestamp createdAt;

    private Timestamp modifiedAt;
}
