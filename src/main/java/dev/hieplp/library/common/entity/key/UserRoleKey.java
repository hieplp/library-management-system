package dev.hieplp.library.common.entity.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleKey implements Serializable {
    @Column(name = "user_id", length = 100)
    private String userId;

    @Column(name = "role", length = 1)
    private Byte role;
}
