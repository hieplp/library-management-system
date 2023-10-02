package dev.hieplp.library.common.helper.impl;

import dev.hieplp.library.common.entity.*;
import dev.hieplp.library.common.enums.location.AddressStatus;
import dev.hieplp.library.common.exception.NotFoundException;
import dev.hieplp.library.common.helper.LocationHelper;
import dev.hieplp.library.common.util.ObjectUtil;
import dev.hieplp.library.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LocationHelperImpl implements LocationHelper {

    private final CountryRepository countryRepo;
    private final CityRepository cityRepo;
    private final DistrictRepository districtRepo;
    private final WardRepository wardRepo;
    private final AddressRepository addressRepo;

    private final ObjectUtil objectUtil;

    @Override
    public Country getCountry(String countryId) {
        log.info("Get country with id: {}", countryId);
        return countryRepo.findById(countryId)
                .orElseThrow(() -> {
                    var message = String.format("Country with id: %s not found", countryId);
                    log.warn(message);
                    return new NotFoundException(message);
                });
    }

    @Override
    public <T> T getCountry(String countryId, Class<T> type) {
        log.info("Get country with id: {}", countryId);
        var country = getCountry(countryId);
        return objectUtil.parse(country, type);
    }

    @Override
    public City getCity(String cityId) {
        log.info("Get city with id: {}", cityId);
        return cityRepo.findById(cityId)
                .orElseThrow(() -> {
                    var message = String.format("City with id: %s not found", cityId);
                    log.warn(message);
                    return new NotFoundException(message);
                });
    }

    @Override
    public City getCity(String cityId, String countryId) {
        log.info("Get city with cityId: {} and countryId: {}", cityId, countryId);

        var city = getCity(cityId);
        if (city.getCountry().getCountryId().equals(countryId)) {
            return city;
        }

        var message = String.format("City with id: %s not found in country with id: %s", cityId, countryId);
        log.warn(message);
        throw new NotFoundException(message);
    }

    @Override
    public <T> T getCity(String cityId, Class<T> type) {
        log.info("Get city with id: {}", cityId);
        var city = getCity(cityId);
        return objectUtil.parse(city, type);
    }

    @Override
    public District getDistrict(String districtId) {
        log.info("Get district with id: {}", districtId);
        return districtRepo.findById(districtId)
                .orElseThrow(() -> {
                    var message = String.format("District with id: %s not found", districtId);
                    log.warn(message);
                    return new NotFoundException(message);
                });
    }

    @Override
    public District getDistrict(String districtId, String cityId) {
        log.info("Get district with districtId: {} and cityId: {}", districtId, cityId);

        var district = getDistrict(districtId);
        if (district.getCity().getCityId().equals(cityId)) {
            return district;
        }

        var message = String.format("District with id: %s not found in city with id: %s", districtId, cityId);
        log.warn(message);
        throw new NotFoundException(message);
    }

    @Override
    public <T> T getDistrict(String districtId, Class<T> type) {
        log.info("Get district with id: {}", districtId);
        var district = getDistrict(districtId);
        return objectUtil.parse(district, type);
    }

    @Override
    public Ward getWard(String wardId) {
        log.info("Get ward with id: {}", wardId);
        return wardRepo.findById(wardId)
                .orElseThrow(() -> {
                    var message = String.format("Ward with id: %s not found", wardId);
                    log.warn(message);
                    return new NotFoundException(message);
                });
    }

    @Override
    public Ward getWard(String wardId, String districtId) {
        log.info("Get ward with wardId: {} and districtId: {}", wardId, districtId);

        var ward = getWard(wardId);
        if (ward.getDistrict().getDistrictId().equals(districtId)) {
            return ward;
        }

        var message = String.format("Ward with id: %s not found in district with id: %s", wardId, districtId);
        log.warn(message);
        throw new NotFoundException(message);
    }

    @Override
    public <T> T getWard(String wardId, Class<T> type) {
        log.info("Get ward with id: {}", wardId);
        var ward = getWard(wardId);
        return objectUtil.parse(ward, type);
    }

    @Override
    public Address getAddress(String addressId) {
        log.info("Get address with id: {}", addressId);
        return addressRepo.findById(addressId)
                .orElseThrow(() -> {
                    var message = String.format("Address with id: %s not found", addressId);
                    log.warn(message);
                    return new NotFoundException(message);
                });
    }

    @Override
    public Address getAddress(String addressId, String userId) {
        log.info("Get address with addressId: {} and userId: {}", addressId, userId);

        var address = getAddress(addressId);
        if (address.getUser().getUserId().equals(userId)) {
            return address;
        }

        var message = String.format("Address with id: %s not found in user with id: %s", addressId, userId);
        log.warn(message);
        throw new NotFoundException(message);
    }

    @Override
    public Address getActiveAddress(String addressId, String userId) {
        log.info("Get active address with addressId: {} and userId: {}", addressId, userId);

        var address = getAddress(addressId, userId);
        if (AddressStatus.ACTIVE.getStatus().equals(address.getStatus())) {
            return address;
        }

        var message = String.format("Address with id: %s is not active", addressId);
        log.warn(message);
        throw new NotFoundException(message);
    }

    @Override
    public <T> T getAddress(String addressId, Class<T> type) {
        log.info("Get address with id: {}", addressId);
        var address = getAddress(addressId);
        return objectUtil.parse(address, type);
    }
}
