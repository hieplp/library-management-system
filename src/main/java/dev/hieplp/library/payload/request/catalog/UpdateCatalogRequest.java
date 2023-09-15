package dev.hieplp.library.payload.request.catalog;

import lombok.Data;

@Data
public class UpdateCatalogRequest {
    private String catalogName;

    private String description;

    private String parentCatalogId;

    private Byte status;
}
