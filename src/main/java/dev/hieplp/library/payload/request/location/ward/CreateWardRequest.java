package dev.hieplp.library.payload.request.location.ward;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateWardRequest {

    @NotBlank(message = "Ward id is required")
    private String wardId;

    @NotBlank(message = "Ward name is required")
    private String wardName;

    @NotBlank(message = "District id is required")
    private String districtId;

    private String description;
}
