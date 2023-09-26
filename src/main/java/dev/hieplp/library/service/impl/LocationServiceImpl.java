package dev.hieplp.library.service.impl;

import dev.hieplp.library.common.entity.*;
import dev.hieplp.library.common.enums.IdLength;
import dev.hieplp.library.common.enums.location.*;
import dev.hieplp.library.common.exception.DuplicatedException;
import dev.hieplp.library.common.helper.LocationHelper;
import dev.hieplp.library.common.payload.request.GetListRequest;
import dev.hieplp.library.common.payload.response.GetListResponse;
import dev.hieplp.library.common.util.DateTimeUtil;
import dev.hieplp.library.common.util.GeneratorUtil;
import dev.hieplp.library.common.util.ObjectUtil;
import dev.hieplp.library.common.util.SqlUtil;
import dev.hieplp.library.config.security.CurrentUser;
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
import dev.hieplp.library.repository.*;
import dev.hieplp.library.service.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final CurrentUser currentUser;

    private final CountryRepository countryRepo;
    private final CityRepository cityRepo;
    private final DistrictRepository districtRepo;
    private final WardRepository wardRepo;
    private final AddressRepository addressRepo;

    private final SqlUtil sqlUtil;
    private final DateTimeUtil dateTimeUtil;
    private final ObjectUtil objectUtil;
    private final GeneratorUtil generatorUtil;

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
        response.setCountryId(country.getCountryId());
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
        response.setCityId(city.getCityId());
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
        response.setDistrictId(district.getDistrictId());
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

    @Override
    public List<UserCountryResponse> getAllCountriesByUser() {
        log.info("Get all countries by user");
        var countries = countryRepo.findAllByStatus(CountryStatus.ACTIVE.getStatus());
        return objectUtil.copyProperties(countries, UserCountryResponse.class);
    }

    @Override
    public List<UserCityResponse> getAllCitiesByUser() {
        log.info("Get all cities by user");
        var cities = cityRepo.findAllByStatus(CityStatus.ACTIVE.getStatus());
        return objectUtil.copyProperties(cities, UserCityResponse.class);
    }

    @Override
    public List<UserCityResponse> getCitiesByUser(String countryId) {
        log.info("Get cities by user with countryId: {}", countryId);
        var country = locationHelper.getCountry(countryId);
        var cities = cityRepo.findAllByStatusAndCountry(CityStatus.ACTIVE.getStatus(), country);
        return objectUtil.copyProperties(cities, UserCityResponse.class);
    }

    @Override
    public List<UserDistrictResponse> getAllDistrictsByUser() {
        log.info("Get all districts by user");
        var districts = districtRepo.findAllByStatus(DistrictStatus.ACTIVE.getStatus());
        return objectUtil.copyProperties(districts, UserDistrictResponse.class);
    }

    @Override
    public List<UserDistrictResponse> getDistrictsByUser(String cityId) {
        log.info("Get districts by user with cityId: {}", cityId);
        var city = locationHelper.getCity(cityId);
        var districts = districtRepo.findAllByStatusAndCity(DistrictStatus.ACTIVE.getStatus(), city);
        return objectUtil.copyProperties(districts, UserDistrictResponse.class);
    }

    @Override
    public List<UserWardResponse> getAllWardsByUser() {
        log.info("Get all wards by user");
        var wards = wardRepo.findAllByStatus(WardStatus.ACTIVE.getStatus());
        return objectUtil.copyProperties(wards, UserWardResponse.class);
    }

    @Override
    public List<UserWardResponse> getWardsByUser(String districtId) {
        log.info("Get wards by user with districtId: {}", districtId);
        var district = locationHelper.getDistrict(districtId);
        var wards = wardRepo.findAllByStatusAndDistrict(WardStatus.ACTIVE.getStatus(), district);
        return objectUtil.copyProperties(wards, UserWardResponse.class);
    }

    @Override
    public UserAddressResponse createAddressByUser(CreateAddressRequest request) {
        log.info("Create address by user with request: {}", request);

        var user = new User();
        user.setUserId(currentUser.getUserId());

        var country = locationHelper.getCountry(request.getCountryId());
        var city = locationHelper.getCity(request.getCityId(), country.getCountryId());
        var district = locationHelper.getDistrict(request.getDistrictId(), city.getCityId());
        var ward = locationHelper.getWard(request.getWardId(), district.getDistrictId());

        var isDefault = AddressDefault.NO;
        if (!addressRepo.existsByUserAndIsDefaultTrue(user)) {
            isDefault = AddressDefault.YES;
        }

        var type = AddressType.fromValue(request.getType());

        var addressId = generatorUtil.generateId(IdLength.ADDRESS_ID);
        var address = Address.builder()
                .addressId(addressId)
                .address(request.getAddress())
                .description(request.getDescription())
                .type(type.getType())
                .isDefault(isDefault.getStatus())
                .status(AddressStatus.ACTIVE.getStatus())
                .user(user)

                .ward(ward)
                .district(district)
                .city(city)
                .country(country)

                .createdBy(currentUser.getUserId())
                .createdAt(dateTimeUtil.getCurrentTimestamp())
                .modifiedBy(currentUser.getUserId())
                .modifiedAt(dateTimeUtil.getCurrentTimestamp())

                .build();
        addressRepo.save(address);

        var response = new UserAddressResponse();
        BeanUtils.copyProperties(address, response);
        response.setWardId(ward.getWardId());
        response.setWardName(ward.getWardName());
        response.setDistrictId(district.getDistrictId());
        response.setDistrictName(district.getDistrictName());
        response.setCityId(city.getCityId());
        response.setCityName(city.getCityName());
        response.setCountryId(country.getCountryId());
        response.setCountryName(country.getCountryName());
        return response;
    }

    @Override
    public UserAddressResponse updateAddressByUser(String addressId, UpdateAddressRequest request) {
        log.info("Update address by user with addressId: {} and request: {}", addressId, request);

        var address = locationHelper.getAddress(addressId, currentUser.getUserId());

        var isChanged = false;

        if (request.getAddress() != null && !address.getAddress().equals(request.getAddress())) {
            address.setAddress(request.getAddress());
            isChanged = true;
        }

        if (request.getDescription() != null && !address.getDescription().equals(request.getDescription())) {
            address.setDescription(request.getDescription());
            isChanged = true;
        }

        if (request.getType() != null && !address.getType().equals(request.getType())) {
            var type = AddressType.fromValue(request.getType());
            address.setType(type.getType());
            isChanged = true;
        }

        if (request.getIsDefault() != null && !address.getIsDefault().equals(request.getIsDefault())) {
            if (AddressDefault.YES.getStatus().equals(request.getIsDefault())) {
                // TODO: Update old default address into NO
//                addressRepo.updateIsDefaultByUser(currentUser.getUserId(), AddressDefault.NO.getStatus());
                address.setIsDefault(AddressDefault.YES.getStatus());
                isChanged = true;
            }
        }

        // Update country, city, district, ward
        // TODO: Optimize this. If only one of them is changed, it will query all of them
        if ((request.getCountryId() != null && !address.getCountry().getCountryId().equals(request.getCountryId()))
                || (request.getCityId() != null && !address.getCity().getCityId().equals(request.getCityId()))
                || (request.getDistrictId() != null && !address.getDistrict().getDistrictId().equals(request.getDistrictId()))
                || (request.getWardId() != null && !address.getWard().getWardId().equals(request.getWardId()))) {
            var country = locationHelper.getCountry(request.getCountryId());
            address.setCountry(country);

            var city = locationHelper.getCity(request.getCityId(), country.getCountryId());
            address.setCity(city);

            var district = locationHelper.getDistrict(request.getDistrictId(), city.getCityId());
            address.setDistrict(district);

            var ward = locationHelper.getWard(request.getWardId(), district.getDistrictId());
            address.setWard(ward);

            isChanged = true;
        }

        if (isChanged) {
            address
                    .setModifiedBy(currentUser.getUserId())
                    .setModifiedAt(dateTimeUtil.getCurrentTimestamp());
            addressRepo.save(address);
        }

        var response = new UserAddressResponse();
        BeanUtils.copyProperties(address, response);
        return response;
    }

    @Override
    public void deleteAddressByUser(String addressId) {
        log.info("Delete address by user with addressId: {}", addressId);
        var address = locationHelper.getAddress(addressId, currentUser.getUserId());
        address
                .setStatus(AddressStatus.INACTIVE.getStatus())
                .setModifiedBy(currentUser.getUserId())
                .setModifiedAt(dateTimeUtil.getCurrentTimestamp());
        addressRepo.save(address);
    }

    @Override
    public UserAddressResponse getAddressByUser(String addressId) {
        log.info("Get address by user with addressId: {}", addressId);
        var address = locationHelper.getAddress(addressId, currentUser.getUserId());
        var response = new UserAddressResponse();
        BeanUtils.copyProperties(address, response);
        return response;
    }

    @Override
    public GetListResponse<UserAddressResponse> getAddressesByUser(GetListRequest request) {
        log.info("Get addresses by user with request: {}", request);
        return sqlUtil.getList(request, addressRepo, UserAddressResponse.class);
    }
}
