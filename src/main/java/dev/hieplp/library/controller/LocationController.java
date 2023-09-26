package dev.hieplp.library.controller;

import dev.hieplp.library.common.enums.response.SuccessCode;
import dev.hieplp.library.common.payload.response.CommonResponse;
import dev.hieplp.library.service.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("locations")
public class LocationController {

    private final static String COUNTRY_PATH = "countries";
    private final static String CITY_PATH = "cities";
    private final static String DISTRICT_PATH = "districts";
    private final static String WARD_PATH = "wards";

    private final LocationService locationService;

    @GetMapping(COUNTRY_PATH)
    public ResponseEntity<CommonResponse> getAllCountriesByUser() {
        log.info("Get all countries");
        var response = locationService.getAllCountriesByUser();
        return ResponseEntity.ok(new CommonResponse(SuccessCode.SUCCESS, response));
    }

    @GetMapping(CITY_PATH)
    public ResponseEntity<CommonResponse> getAllCitiesByUser() {
        log.info("Get all cities");
        var response = locationService.getAllCitiesByUser();
        return ResponseEntity.ok(new CommonResponse(SuccessCode.SUCCESS, response));
    }

    @GetMapping(COUNTRY_PATH + "/{countryId}/" + CITY_PATH)
    public ResponseEntity<CommonResponse> getCitiesByUser(@PathVariable String countryId) {
        log.info("Get country with id: {}", countryId);
        var response = locationService.getCitiesByUser(countryId);
        return ResponseEntity.ok(new CommonResponse(SuccessCode.SUCCESS, response));
    }

    @GetMapping(DISTRICT_PATH)
    public ResponseEntity<CommonResponse> getAllDistrictsByUser() {
        log.info("Get all districts");
        var response = locationService.getAllDistrictsByUser();
        return ResponseEntity.ok(new CommonResponse(SuccessCode.SUCCESS, response));
    }

    @GetMapping(CITY_PATH + "/{cityId}/" + DISTRICT_PATH)
    public ResponseEntity<CommonResponse> getDistrictsByUser(@PathVariable String cityId) {
        log.info("Get districts with cityId: {}", cityId);
        var response = locationService.getDistrictsByUser(cityId);
        return ResponseEntity.ok(new CommonResponse(SuccessCode.SUCCESS, response));
    }

    @GetMapping(WARD_PATH)
    public ResponseEntity<CommonResponse> getAllWardsByUser() {
        log.info("Get all wards");
        var response = locationService.getAllWardsByUser();
        return ResponseEntity.ok(new CommonResponse(SuccessCode.SUCCESS, response));
    }

    @GetMapping(DISTRICT_PATH + "/{districtId}/" + WARD_PATH)
    public ResponseEntity<CommonResponse> getWardsByUser(@PathVariable String districtId) {
        log.info("Get wards with districtId: {}", districtId);
        var response = locationService.getWardsByUser(districtId);
        return ResponseEntity.ok(new CommonResponse(SuccessCode.SUCCESS, response));
    }
}
