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
public class Password {
    @Id
    private String userId;

    @OneToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private User user;

    @Column(length = 64)
    private byte[] password;

    @Column(length = 64)
    private byte[] salt;
}
