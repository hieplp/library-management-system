package dev.hieplp.library.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.sql.Timestamp;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class User {
    @Id
    private String userId;

    @Column(unique = true,
            length = 100)
    private String username;

    @Column(unique = true,
            length = 100)
    private String email;

    @Column(length = 100)
    private String fullName;

    @Column(length = 100)
    private String avatar;

    @Column(length = 1)
    private Byte status;

    private String rootToken;

    private String createdBy;

    private Timestamp createdAt;

    private String modifiedBy;

    private Timestamp modifiedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<UserRole> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Address> addresses;
}
