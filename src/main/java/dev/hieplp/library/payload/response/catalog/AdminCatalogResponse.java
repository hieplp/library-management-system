package dev.hieplp.library.payload.response.catalog;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;

@Data
@EqualsAndHashCode(callSuper = true)
public class AdminCatalogResponse extends CommonCatalogResponse {
    private Byte status;
    private String createdBy;
    private Timestamp createdAt;
    private String modifiedBy;
    private Timestamp modifiedAt;
}
