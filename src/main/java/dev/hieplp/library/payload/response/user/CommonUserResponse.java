package dev.hieplp.library.payload.response.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CommonUserResponse {
    private String userId;
    private String username;
    private String email;
    private String fullName;
    private String avatar;
    private Byte status;
}
