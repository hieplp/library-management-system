package dev.hieplp.library.payload.request.location.district;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateDistrictRequest {

    @NotBlank(message = "District id is required")
    private String districtId;

    @NotBlank(message = "Country name is required")
    private String districtName;

    @NotBlank(message = "City id is required")
    private String cityId;

    private String description;
}
