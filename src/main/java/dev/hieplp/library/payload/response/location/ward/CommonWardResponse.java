package dev.hieplp.library.payload.response.location.ward;

import lombok.Data;

@Data
public class CommonWardResponse {
    private String districtId;
    private String wardId;
    private String wardName;
    private String description;
}
