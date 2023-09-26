package dev.hieplp.library.service;

import dev.hieplp.library.common.payload.request.GetListRequest;
import dev.hieplp.library.common.payload.response.GetListResponse;
import dev.hieplp.library.payload.request.location.address.CreateAddressRequest;
import dev.hieplp.library.payload.request.location.address.UpdateAddressRequest;
import dev.hieplp.library.payload.request.location.city.CreateCityRequest;
import dev.hieplp.library.payload.request.location.country.CreateCountryRequest;
import dev.hieplp.library.payload.request.location.district.CreateDistrictRequest;
import dev.hieplp.library.payload.request.location.ward.CreateWardRequest;
import dev.hieplp.library.payload.response.location.address.UserAddressResponse;
import dev.hieplp.library.payload.response.location.city.AdminCityResponse;
import dev.hieplp.library.payload.response.location.city.UserCityResponse;
import dev.hieplp.library.payload.response.location.country.AdminCountryResponse;
import dev.hieplp.library.payload.response.location.country.UserCountryResponse;
import dev.hieplp.library.payload.response.location.district.AdminDistrictResponse;
import dev.hieplp.library.payload.response.location.district.UserDistrictResponse;
import dev.hieplp.library.payload.response.location.ward.AdminWardResponse;
import dev.hieplp.library.payload.response.location.ward.UserWardResponse;

import java.util.List;

public interface LocationService {
    // ------------------- ADMIN : COUNTRY -------------------
    AdminCountryResponse createCountryByAdmin(CreateCountryRequest request);

    AdminCountryResponse getCountryByAdmin(String countryId);

    GetListResponse<AdminCountryResponse> getCountriesByAdmin(GetListRequest request);

    // ------------------- ADMIN : CITY -------------------
    AdminCityResponse createCityByAdmin(CreateCityRequest request);

    AdminCityResponse getCityByAdmin(String cityId);

    GetListResponse<AdminCityResponse> getCitiesByAdmin(GetListRequest request);

    // ------------------- ADMIN : DISTRICT -------------------
    AdminDistrictResponse createDistrictByAdmin(CreateDistrictRequest request);

    AdminDistrictResponse getDistrictByAdmin(String districtId);

    GetListResponse<AdminDistrictResponse> getDistrictsByAdmin(GetListRequest request);

    // ------------------- ADMIN : WARD -------------------
    AdminWardResponse createWardByAdmin(CreateWardRequest request);

    AdminWardResponse getWardByAdmin(String wardId);

    GetListResponse<AdminWardResponse> getWardsByAdmin(GetListRequest request);

    // ------------------- USER : COUNTRY -------------------
    List<UserCountryResponse> getAllCountriesByUser();

    // ------------------- USER : CITY -------------------
    List<UserCityResponse> getAllCitiesByUser();

    List<UserCityResponse> getCitiesByUser(String countryId);

    // ------------------- USER : DISTRICT -------------------
    List<UserDistrictResponse> getAllDistrictsByUser();

    List<UserDistrictResponse> getDistrictsByUser(String cityId);

    // ------------------- USER : WARD -------------------
    List<UserWardResponse> getAllWardsByUser();

    List<UserWardResponse> getWardsByUser(String districtId);

    // ------------------- USER : ADDRESS -------------------
    UserAddressResponse createAddressByUser(CreateAddressRequest request);

    UserAddressResponse updateAddressByUser(String addressId, UpdateAddressRequest request);

    void deleteAddressByUser(String addressId);

    UserAddressResponse getAddressByUser(String addressId);

    GetListResponse<UserAddressResponse> getAddressesByUser(GetListRequest request);
}
