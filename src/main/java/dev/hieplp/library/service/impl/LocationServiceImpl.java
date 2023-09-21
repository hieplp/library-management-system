package dev.hieplp.library.service.impl;

import dev.hieplp.library.common.entity.City;
import dev.hieplp.library.common.entity.Country;
import dev.hieplp.library.common.enums.location.CityStatus;
import dev.hieplp.library.common.enums.location.CountryStatus;
import dev.hieplp.library.common.exception.DuplicatedException;
import dev.hieplp.library.common.helper.LocationHelper;
import dev.hieplp.library.common.payload.request.GetListRequest;
import dev.hieplp.library.common.payload.response.GetListResponse;
import dev.hieplp.library.common.util.DateTimeUtil;
import dev.hieplp.library.common.util.SqlUtil;
import dev.hieplp.library.config.security.CurrentUser;
import dev.hieplp.library.payload.request.location.city.CreateCityRequest;
import dev.hieplp.library.payload.request.location.country.CreateCountryRequest;
import dev.hieplp.library.payload.response.location.city.AdminCityResponse;
import dev.hieplp.library.payload.response.location.country.AdminCountryResponse;
import dev.hieplp.library.repository.CityRepository;
import dev.hieplp.library.repository.CountryRepository;
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
}
