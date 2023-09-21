package dev.hieplp.library.payload.request.location.country;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateCountryRequest {
    @NotBlank(message = "Country id is required")
    private String countryId;

    @NotBlank(message = "Country name is required")
    private String countryName;

    private String description;
}
