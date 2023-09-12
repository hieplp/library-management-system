package dev.hieplp.library.common.entity;

import dev.hieplp.library.common.entity.key.UserRoleKey;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRole {
    @EmbeddedId
    private UserRoleKey id;

    @ManyToOne(cascade = CascadeType.ALL)
    @MapsId("user_id")
    @JoinColumn(name = "user_id")
    private User user;
}
