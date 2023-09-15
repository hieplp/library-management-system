package dev.hieplp.library.payload.response.catalog;

import lombok.Data;

@Data
public class CommonCatalogResponse {
    private String catalogId;
    private String catalogName;
    private String description;
    private String parentCatalog;
}
