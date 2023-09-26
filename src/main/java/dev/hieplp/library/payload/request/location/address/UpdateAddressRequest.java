package dev.hieplp.library.payload.request.location.address;

import lombok.Data;

@Data
public class UpdateAddressRequest {
    private String address;

    private String wardId;

    private String districtId;

    private String cityId;

    private String countryId;

    private String description;

    private Byte type;

    private Byte isDefault;
}
