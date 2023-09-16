package dev.hieplp.library.common.payload.request;

import lombok.Data;

@Data
public class GetListRequest {
    private String searchBy;
    private String searchValue;

    private String filterBy;
    private String filterValue;

    private int page;
    private int size;

    private String order;
    private String orderBy;

    private Byte[] statuses;
}
