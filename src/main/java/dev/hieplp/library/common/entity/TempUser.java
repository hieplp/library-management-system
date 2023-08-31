package dev.hieplp.library.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TempUser {
    @Id
    @Column(length = 100)
    private String userId;

    @Column(length = 100)
    private String username;

    @Column(length = 100)
    private String email;

    @Column(length = 64)
    private byte[] password;

    @Column(length = 64)
    private byte[] salt;

    @Column(length = 1)
    private Byte status;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "otp_id")
    private Otp otp;
}
