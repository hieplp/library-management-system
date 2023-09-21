package dev.hieplp.library.service;

import dev.hieplp.library.common.payload.request.GetListRequest;
import dev.hieplp.library.common.payload.response.GetListResponse;
import dev.hieplp.library.payload.request.location.city.CreateCityRequest;
import dev.hieplp.library.payload.request.location.country.CreateCountryRequest;
import dev.hieplp.library.payload.response.location.city.AdminCityResponse;
import dev.hieplp.library.payload.response.location.country.AdminCountryResponse;

public interface LocationService {
    AdminCountryResponse createCountryByAdmin(CreateCountryRequest request);

    AdminCountryResponse getCountryByAdmin(String countryId);

    GetListResponse<AdminCountryResponse> getCountriesByAdmin(GetListRequest request);

    AdminCityResponse createCityByAdmin(CreateCityRequest request);

    AdminCityResponse getCityByAdmin(String cityId);

    GetListResponse<AdminCityResponse> getCitiesByAdmin(GetListRequest request);
}
