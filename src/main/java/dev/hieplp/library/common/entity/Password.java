package dev.hieplp.library.common.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Password {
    @Id
    private String userId;

    @OneToOne(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @MapsId("user_id")
    private User user;

    @Column(length = 64)
    private byte[] password;

    @Column(length = 64)
    private byte[] salt;
}
