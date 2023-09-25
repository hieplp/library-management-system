package dev.hieplp.library.payload.response.location.district;

import lombok.Data;

@Data
public class CommonDistrictResponse {
    private String cityId;
    private String districtId;
    private String districtName;
    private String description;
}
