package dev.hieplp.library.common.helper;

import dev.hieplp.library.common.entity.City;
import dev.hieplp.library.common.entity.Country;

public interface LocationHelper {
    Country getCountry(String countryId);

    <T> T getCountry(String countryId, Class<T> type);

    City getCity(String cityId);

    <T> T getCity(String cityId, Class<T> type);
}
