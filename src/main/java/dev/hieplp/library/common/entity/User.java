package dev.hieplp.library.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
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

//    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
//    private Password password;
}
