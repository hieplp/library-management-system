package dev.hieplp.library.common.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;

@Data
@Entity
public class Otp {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String otpId;

    @Column(length = 1)
    private Byte type;

    private String sendTo;

    private String token;

    private Date issueDate;

    private Date expiryDate;

    @Column(length = 1)
    private Byte status;

    private Timestamp createdAt;
    private Timestamp modifiedAt;
}
