package dev.hieplp.library.payload.response.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AdminUserResponse extends CommonUserResponse {
    private String createdBy;
    private Timestamp createdAt;
    private String modifiedBy;
    private Timestamp modifiedAt;
}
