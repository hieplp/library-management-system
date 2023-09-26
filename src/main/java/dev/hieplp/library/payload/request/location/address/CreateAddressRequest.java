package dev.hieplp.library.payload.request.location.address;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateAddressRequest {
    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Ward is required")
    private String wardId;

    @NotBlank(message = "District is required")
    private String districtId;

    @NotBlank(message = "City is required")
    private String cityId;

    @NotBlank(message = "Country is required")
    private String countryId;

    private String description;

    private Byte type;
}
