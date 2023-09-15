package dev.hieplp.library.common.enums.catalog;

import dev.hieplp.library.common.exception.UnknownException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum CatalogStatus {
    INACTIVE(0),
    ACTIVE(1);

    private final Byte status;

    CatalogStatus(Integer status) {
        this.status = status.byteValue();
    }

    public static CatalogStatus fromStatus(Byte status) {
        return Arrays.stream(values())
                .filter(value -> value.status.equals(status))
                .findFirst()
                .orElseThrow(() -> new UnknownException("Unknown catalog status"));
    }
}
