package dev.hieplp.library.service;

import dev.hieplp.library.common.payload.request.GetListRequest;
import dev.hieplp.library.common.payload.response.GetListResponse;
import dev.hieplp.library.payload.request.location.city.CreateCityRequest;
import dev.hieplp.library.payload.request.location.country.CreateCountryRequest;
import dev.hieplp.library.payload.request.location.district.CreateDistrictRequest;
import dev.hieplp.library.payload.request.location.ward.CreateWardRequest;
import dev.hieplp.library.payload.response.location.city.AdminCityResponse;
import dev.hieplp.library.payload.response.location.country.AdminCountryResponse;
import dev.hieplp.library.payload.response.location.district.AdminDistrictResponse;
import dev.hieplp.library.payload.response.location.ward.AdminWardResponse;

public interface LocationService {
    AdminCountryResponse createCountryByAdmin(CreateCountryRequest request);

    AdminCountryResponse getCountryByAdmin(String countryId);

    GetListResponse<AdminCountryResponse> getCountriesByAdmin(GetListRequest request);

    AdminCityResponse createCityByAdmin(CreateCityRequest request);

    AdminCityResponse getCityByAdmin(String cityId);

    GetListResponse<AdminCityResponse> getCitiesByAdmin(GetListRequest request);

    AdminDistrictResponse createDistrictByAdmin(CreateDistrictRequest request);

    AdminDistrictResponse getDistrictByAdmin(String districtId);

    GetListResponse<AdminDistrictResponse> getDistrictsByAdmin(GetListRequest request);

    AdminWardResponse createWardByAdmin(CreateWardRequest request);

    AdminWardResponse getWardByAdmin(String wardId);

    GetListResponse<AdminWardResponse> getWardsByAdmin(GetListRequest request);
}
