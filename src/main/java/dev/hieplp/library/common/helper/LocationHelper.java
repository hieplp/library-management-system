package dev.hieplp.library.common.helper;

import dev.hieplp.library.common.entity.*;

public interface LocationHelper {
    // ------------------- COUNTRY -------------------
    Country getCountry(String countryId);

    <T> T getCountry(String countryId, Class<T> type);

    // ------------------- CITY -------------------
    City getCity(String cityId);

    City getCity(String cityId, String countryId);

    <T> T getCity(String cityId, Class<T> type);

    // ------------------- DISTRICT -------------------
    District getDistrict(String districtId);

    District getDistrict(String districtId, String cityId);

    <T> T getDistrict(String districtId, Class<T> type);

    // ------------------- WARD -------------------
    Ward getWard(String wardId);

    Ward getWard(String wardId, String districtId);

    <T> T getWard(String wardId, Class<T> type);

    // ------------------- ADDRESS -------------------
    Address getAddress(String addressId);

    Address getAddress(String addressId, String userId);

    Address getActiveAddress(String addressId, String userId);

    <T> T getAddress(String addressId, Class<T> type);
}
