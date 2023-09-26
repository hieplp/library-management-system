package dev.hieplp.library.controller.admin;

import dev.hieplp.library.common.enums.response.SuccessCode;
import dev.hieplp.library.common.payload.request.GetListRequest;
import dev.hieplp.library.common.payload.response.CommonResponse;
import dev.hieplp.library.payload.request.location.city.CreateCityRequest;
import dev.hieplp.library.payload.request.location.country.CreateCountryRequest;
import dev.hieplp.library.payload.request.location.district.CreateDistrictRequest;
import dev.hieplp.library.payload.request.location.ward.CreateWardRequest;
import dev.hieplp.library.service.LocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/locations")
public class AdminLocationController {

    private final static String COUNTRY_PATH = "countries";
    private final static String CITY_PATH = "cities";
    private final static String DISTRICT_PATH = "districts";
    private final static String WARD_PATH = "wards";

    private final LocationService locationService;

    // ------------------- COUNTRY -------------------
    @PostMapping(COUNTRY_PATH)
    public ResponseEntity<CommonResponse> createCountryByAdmin(@Valid @RequestBody CreateCountryRequest request) {
        log.info("Create country with request: {}", request);
        var response = locationService.createCountryByAdmin(request);
        return ResponseEntity.ok(new CommonResponse(SuccessCode.SUCCESS, response));
    }

    @GetMapping(COUNTRY_PATH)
    public ResponseEntity<CommonResponse> getCountriesByAdmin(GetListRequest request) {
        log.info("Get countries with request: {}", request);
        var response = locationService.getCountriesByAdmin(request);
        return ResponseEntity.ok(new CommonResponse(SuccessCode.SUCCESS, response));
    }

    @GetMapping(COUNTRY_PATH + "/{countryId}")
    public ResponseEntity<CommonResponse> getCountryByAdmin(@PathVariable String countryId) {
        log.info("Get country with id: {}", countryId);
        var response = locationService.getCountryByAdmin(countryId);
        return ResponseEntity.ok(new CommonResponse(SuccessCode.SUCCESS, response));
    }

    // ------------------- CITY -------------------
    @PostMapping(CITY_PATH)
    public ResponseEntity<CommonResponse> createCityByAdmin(@Valid @RequestBody CreateCityRequest request) {
        log.info("Create city with request: {}", request);
        var response = locationService.createCityByAdmin(request);
        return ResponseEntity.ok(new CommonResponse(SuccessCode.SUCCESS, response));
    }

    @GetMapping(CITY_PATH)
    public ResponseEntity<CommonResponse> getCitiesByAdmin(GetListRequest request) {
        log.info("Get cities with request: {}", request);
        var response = locationService.getCitiesByAdmin(request);
        return ResponseEntity.ok(new CommonResponse(SuccessCode.SUCCESS, response));
    }

    @GetMapping(CITY_PATH + "/{cityId}")
    public ResponseEntity<CommonResponse> getCityByAdmin(@PathVariable String cityId) {
        log.info("Get city with id: {}", cityId);
        var response = locationService.getCityByAdmin(cityId);
        return ResponseEntity.ok(new CommonResponse(SuccessCode.SUCCESS, response));
    }

    // ------------------- DISTRICT -------------------
    @PostMapping(DISTRICT_PATH)
    public ResponseEntity<CommonResponse> createDistrictByAdmin(@Valid @RequestBody CreateDistrictRequest request) {
        log.info("Create district with request: {}", request);
        var response = locationService.createDistrictByAdmin(request);
        return ResponseEntity.ok(new CommonResponse(SuccessCode.SUCCESS, response));
    }

    @GetMapping(DISTRICT_PATH)
    public ResponseEntity<CommonResponse> getDistrictsByAdmin(GetListRequest request) {
        log.info("Get districts with request: {}", request);
        var response = locationService.getDistrictsByAdmin(request);
        return ResponseEntity.ok(new CommonResponse(SuccessCode.SUCCESS, response));
    }

    @GetMapping(DISTRICT_PATH + "/{districtId}")
    public ResponseEntity<CommonResponse> getDistrictByAdmin(@PathVariable String districtId) {
        log.info("Get district with id: {}", districtId);
        var response = locationService.getDistrictByAdmin(districtId);
        return ResponseEntity.ok(new CommonResponse(SuccessCode.SUCCESS, response));
    }

    // ------------------- WARD -------------------
    @PostMapping(WARD_PATH)
    public ResponseEntity<CommonResponse> createWardByAdmin(@Valid @RequestBody CreateWardRequest request) {
        log.info("Create ward with request: {}", request);
        var response = locationService.createWardByAdmin(request);
        return ResponseEntity.ok(new CommonResponse(SuccessCode.SUCCESS, response));
    }

    @GetMapping(WARD_PATH)
    public ResponseEntity<CommonResponse> getWardsByAdmin(GetListRequest request) {
        log.info("Get wards with request: {}", request);
        var response = locationService.getWardsByAdmin(request);
        return ResponseEntity.ok(new CommonResponse(SuccessCode.SUCCESS, response));
    }

    @GetMapping(WARD_PATH + "/{wardId}")
    public ResponseEntity<CommonResponse> getWardByAdmin(@PathVariable String wardId) {
        log.info("Get ward with id: {}", wardId);
        var response = locationService.getWardByAdmin(wardId);
        return ResponseEntity.ok(new CommonResponse(SuccessCode.SUCCESS, response));
    }
}
