package dev.hieplp.library.payload.request.catalog;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateCatalogRequest {
    @NotBlank(message = "catalogName is required")
    private String catalogName;

    @NotBlank(message = "description is required")
    private String description;

    private String parentCatalogId;
}
