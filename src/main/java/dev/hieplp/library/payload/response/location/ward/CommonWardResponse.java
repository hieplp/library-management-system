package dev.hieplp.library.payload.response.location.ward;

import lombok.Data;

@Data
public class CommonWardResponse {
    private String cityId;
    private String districtId;
    private String districtName;
    private String description;
}
