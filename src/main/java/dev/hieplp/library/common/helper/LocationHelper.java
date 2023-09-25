package dev.hieplp.library.common.helper;

import dev.hieplp.library.common.entity.City;
import dev.hieplp.library.common.entity.Country;
import dev.hieplp.library.common.entity.District;
import dev.hieplp.library.common.entity.Ward;

public interface LocationHelper {
    Country getCountry(String countryId);

    <T> T getCountry(String countryId, Class<T> type);

    City getCity(String cityId);

    <T> T getCity(String cityId, Class<T> type);

    District getDistrict(String districtId);

    <T> T getDistrict(String districtId, Class<T> type);

    Ward getWard(String wardId);

    <T> T getWard(String wardId, Class<T> type);
}
