package dev.hieplp.library.payload.request.location.city;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateCityRequest {

    @NotBlank(message = "City id is required")
    private String cityId;

    @NotBlank(message = "City name is required")
    private String cityName;

    @NotBlank(message = "Country id is required")
    private String countryId;

    private String description;
}
