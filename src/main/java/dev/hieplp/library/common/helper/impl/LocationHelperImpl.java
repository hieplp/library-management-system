package dev.hieplp.library.common.helper.impl;

import dev.hieplp.library.common.entity.City;
import dev.hieplp.library.common.entity.Country;
import dev.hieplp.library.common.exception.NotFoundException;
import dev.hieplp.library.common.exception.UnknownException;
import dev.hieplp.library.common.helper.LocationHelper;
import dev.hieplp.library.repository.CityRepository;
import dev.hieplp.library.repository.CountryRepository;
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
        try {
            log.info("Get country with id: {}", countryId);
            var country = getCountry(countryId);
            var response = type.getConstructor().newInstance();
            BeanUtils.copyProperties(country, response);
            return response;
        } catch (InstantiationException
                 | IllegalAccessException
                 | InvocationTargetException
                 | NoSuchMethodException e) {
            log.error(e.getMessage());
            throw new UnknownException(e.getMessage());
        }
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
        try {
            log.info("Get city with id: {}", cityId);
            var city = getCity(cityId);
            var response = type.getConstructor().newInstance();
            BeanUtils.copyProperties(city, response);
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
