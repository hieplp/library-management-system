package dev.hieplp.library.service.impl;

import dev.hieplp.library.common.entity.City;
import dev.hieplp.library.common.entity.Country;
import dev.hieplp.library.common.entity.District;
import dev.hieplp.library.common.entity.Ward;
import dev.hieplp.library.common.enums.location.CityStatus;
import dev.hieplp.library.common.enums.location.CountryStatus;
import dev.hieplp.library.common.enums.location.DistrictStatus;
import dev.hieplp.library.common.enums.location.WardStatus;
import dev.hieplp.library.common.exception.DuplicatedException;
import dev.hieplp.library.common.helper.LocationHelper;
import dev.hieplp.library.common.payload.request.GetListRequest;
import dev.hieplp.library.common.payload.response.GetListResponse;
import dev.hieplp.library.common.util.DateTimeUtil;
import dev.hieplp.library.common.util.SqlUtil;
import dev.hieplp.library.config.security.CurrentUser;
import dev.hieplp.library.payload.request.location.city.CreateCityRequest;
import dev.hieplp.library.payload.request.location.country.CreateCountryRequest;
import dev.hieplp.library.payload.request.location.district.CreateDistrictRequest;
import dev.hieplp.library.payload.request.location.ward.CreateWardRequest;
import dev.hieplp.library.payload.response.location.city.AdminCityResponse;
import dev.hieplp.library.payload.response.location.country.AdminCountryResponse;
import dev.hieplp.library.payload.response.location.district.AdminDistrictResponse;
import dev.hieplp.library.payload.response.location.ward.AdminWardResponse;
import dev.hieplp.library.repository.CityRepository;
import dev.hieplp.library.repository.CountryRepository;
import dev.hieplp.library.repository.DistrictRepository;
import dev.hieplp.library.repository.WardRepository;
import dev.hieplp.library.service.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final CurrentUser currentUser;

    private final CountryRepository countryRepo;
    private final CityRepository cityRepo;
    private final DistrictRepository districtRepo;
    private final WardRepository wardRepo;

    private final SqlUtil sqlUtil;
    private final DateTimeUtil dateTimeUtil;

    private final LocationHelper locationHelper;

    @Override
    public AdminCountryResponse createCountryByAdmin(CreateCountryRequest request) {
        log.info("Create country with request: {}", request);

        if (countryRepo.existsByCountryId(request.getCountryId())) {
            var message = String.format("Country with id: %s already exists", request.getCountryId());
            log.warn(message);
            throw new DuplicatedException(message);
        }

        var country = Country.builder()
                .countryId(request.getCountryId())
                .countryName(request.getCountryName())
                .description(request.getDescription())
                .status(CountryStatus.ACTIVE.getStatus())
                .createdBy(currentUser.getUserId())
                .createdAt(dateTimeUtil.getCurrentTimestamp())
                .modifiedBy(currentUser.getUserId())
                .modifiedAt(dateTimeUtil.getCurrentTimestamp())
                .build();
        countryRepo.save(country);

        var response = new AdminCountryResponse();
        BeanUtils.copyProperties(country, response);
        return response;
    }

    @Override
    public AdminCountryResponse getCountryByAdmin(String countryId) {
        log.info("Get country with id: {}", countryId);
        return locationHelper.getCountry(countryId, AdminCountryResponse.class);
    }

    @Override
    public GetListResponse<AdminCountryResponse> getCountriesByAdmin(GetListRequest request) {
        log.info("Get countries with request: {}", request);
        return sqlUtil.getList(request, countryRepo, AdminCountryResponse.class);
    }

    @Override
    public AdminCityResponse createCityByAdmin(CreateCityRequest request) {
        log.info("Create city with request: {}", request);

        if (cityRepo.existsByCityId(request.getCityId())) {
            var message = String.format("City with id: %s already exists", request.getCityId());
            log.warn(message);
            throw new DuplicatedException(message);
        }

        var country = locationHelper.getCountry(request.getCountryId(), Country.class);

        var city = City.builder()
                .cityId(request.getCityId())
                .cityName(request.getCityName())
                .description(request.getDescription())
                .status(CityStatus.ACTIVE.getStatus())
                .country(country)
                .createdBy(currentUser.getUserId())
                .createdAt(dateTimeUtil.getCurrentTimestamp())
                .modifiedBy(currentUser.getUserId())
                .modifiedAt(dateTimeUtil.getCurrentTimestamp())
                .build();
        cityRepo.save(city);

        var response = new AdminCityResponse();
        BeanUtils.copyProperties(city, response);
        return response;
    }

    @Override
    public AdminCityResponse getCityByAdmin(String cityId) {
        log.info("Get city with id: {}", cityId);
        return locationHelper.getCity(cityId, AdminCityResponse.class);
    }

    @Override
    public GetListResponse<AdminCityResponse> getCitiesByAdmin(GetListRequest request) {
        log.info("Get cities with request: {}", request);
        return sqlUtil.getList(request, cityRepo, AdminCityResponse.class);
    }

    @Override
    public AdminDistrictResponse createDistrictByAdmin(CreateDistrictRequest request) {
        log.info("Create district with request: {}", request);

        if (districtRepo.existsByDistrictId(request.getDistrictId())) {
            var message = String.format("District with id: %s already exists", request.getDistrictId());
            log.warn(message);
            throw new DuplicatedException(message);
        }

        var city = locationHelper.getCity(request.getCityId(), City.class);

        var district = District.builder()
                .districtId(request.getDistrictId())
                .districtName(request.getDistrictName())
                .description(request.getDescription())
                .status(DistrictStatus.ACTIVE.getStatus())
                .city(city)
                .createdBy(currentUser.getUserId())
                .createdAt(dateTimeUtil.getCurrentTimestamp())
                .modifiedBy(currentUser.getUserId())
                .modifiedAt(dateTimeUtil.getCurrentTimestamp())
                .build();
        districtRepo.save(district);

        var response = new AdminDistrictResponse();
        BeanUtils.copyProperties(district, response);
        return response;
    }

    @Override
    public AdminDistrictResponse getDistrictByAdmin(String districtId) {
        log.info("Get district with id: {}", districtId);
        return locationHelper.getDistrict(districtId, AdminDistrictResponse.class);
    }

    @Override
    public GetListResponse<AdminDistrictResponse> getDistrictsByAdmin(GetListRequest request) {
        log.info("Get districts with request: {}", request);
        return sqlUtil.getList(request, districtRepo, AdminDistrictResponse.class);
    }

    @Override
    public AdminWardResponse createWardByAdmin(CreateWardRequest request) {
        log.info("Create ward with request: {}", request);

        if (wardRepo.existsByWardId(request.getWardId())) {
            var message = String.format("Ward with id: %s already exists", request.getWardId());
            log.warn(message);
            throw new DuplicatedException(message);
        }

        var district = locationHelper.getDistrict(request.getDistrictId(), District.class);

        var ward = Ward.builder()
                .wardId(request.getWardId())
                .wardName(request.getWardName())
                .description(request.getDescription())
                .status(WardStatus.ACTIVE.getStatus())
                .district(district)
                .createdBy(currentUser.getUserId())
                .createdAt(dateTimeUtil.getCurrentTimestamp())
                .modifiedBy(currentUser.getUserId())
                .modifiedAt(dateTimeUtil.getCurrentTimestamp())
                .build();
        wardRepo.save(ward);

        var response = new AdminWardResponse();
        BeanUtils.copyProperties(ward, response);
        return response;
    }

    @Override
    public AdminWardResponse getWardByAdmin(String wardId) {
        log.info("Get ward with id: {}", wardId);
        return locationHelper.getWard(wardId, AdminWardResponse.class);
    }

    @Override
    public GetListResponse<AdminWardResponse> getWardsByAdmin(GetListRequest request) {
        log.info("Get wards with request: {}", request);
        return sqlUtil.getList(request, wardRepo, AdminWardResponse.class);
    }
}
