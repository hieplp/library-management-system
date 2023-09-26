package dev.hieplp.library.payload.response.location.address;

import lombok.Data;

@Data
public class CommonAddressResponse {
    private String addressId;
    private String address;

    private String wardId;
    private String wardName;

    private String districtId;
    private String districtName;

    private String cityId;
    private String cityName;

    private String countryId;
    private String countryName;

    private Byte type;
    private Byte isPrimary;
}
