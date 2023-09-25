package dev.hieplp.library.common.helper.impl;

import dev.hieplp.library.common.entity.City;
import dev.hieplp.library.common.entity.Country;
import dev.hieplp.library.common.entity.District;
import dev.hieplp.library.common.entity.Ward;
import dev.hieplp.library.common.exception.NotFoundException;
import dev.hieplp.library.common.exception.UnknownException;
import dev.hieplp.library.common.helper.LocationHelper;
import dev.hieplp.library.repository.CityRepository;
import dev.hieplp.library.repository.CountryRepository;
import dev.hieplp.library.repository.DistrictRepository;
import dev.hieplp.library.repository.WardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

@Slf4j
@Component
@RequiredArgsConstructor
public class LocationHelperImpl implements LocationHelper {

    private final CountryRepository countryRepo;
    private final CityRepository cityRepo;
    private final DistrictRepository districtRepo;
    private final WardRepository wardRepo;

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
        return parse(country, type);
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
    public <T> T getCity(String cityId, Class<T> type) {
        log.info("Get city with id: {}", cityId);
        var city = getCity(cityId);
        return parse(city, type);
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
    public <T> T getDistrict(String districtId, Class<T> type) {
        log.info("Get district with id: {}", districtId);
        var district = getDistrict(districtId);
        return parse(district, type);
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
    public <T> T getWard(String wardId, Class<T> type) {
        log.info("Get ward with id: {}", wardId);
        var ward = getWard(wardId);
        return parse(ward, type);
    }

    private <T> T parse(Object source, Class<T> type) {
        try {
            var response = type.getConstructor().newInstance();
            BeanUtils.copyProperties(source, response);
            return response;
        } catch (InstantiationException
                 | IllegalAccessException
                 | InvocationTargetException
                 | NoSuchMethodException e) {
            log.error(e.getMessage());
            throw new UnknownException(e.getMessage());
        }
    }
}
