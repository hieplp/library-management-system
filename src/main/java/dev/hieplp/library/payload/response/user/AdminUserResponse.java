package dev.hieplp.library.payload.response.user;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;

@Data
@EqualsAndHashCode(callSuper = true)
public class AdminUserResponse extends CommonUserResponse {
    private String createdBy;
    private Timestamp createdAt;
    private String modifiedBy;
    private Timestamp modifiedAt;
}
