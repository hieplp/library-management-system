package dev.hieplp.library.common.payload.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetListResponse<T> {
    private List<T> list;
    private long total;
}
