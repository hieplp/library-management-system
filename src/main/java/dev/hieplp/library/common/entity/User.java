package dev.hieplp.library.common.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String userId;

    @Column(unique = true,
            length = 100)
    private String username;

    @Column(unique = true,
            length = 100)
    private String email;

    @Column(length = 1)
    private Byte status;

    private String createdBy;

    private Timestamp createdAt;

    private String modifiedBy;

    private Timestamp modifiedAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Password password;
}
