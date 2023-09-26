package dev.hieplp.library.controller;

import dev.hieplp.library.common.enums.response.SuccessCode;
import dev.hieplp.library.common.payload.request.GetListRequest;
import dev.hieplp.library.common.payload.response.CommonResponse;
import dev.hieplp.library.payload.request.location.address.CreateAddressRequest;
import dev.hieplp.library.payload.request.location.address.UpdateAddressRequest;
import dev.hieplp.library.service.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("locations")
public class LocationController {

    private final static String COUNTRY_PATH = "countries";
    private final static String CITY_PATH = "cities";
    private final static String DISTRICT_PATH = "districts";
    private final static String WARD_PATH = "wards";
    private final static String ADDRESS_PATH = "addresses";

    private final LocationService locationService;

    // ------------------- COUNTRY -------------------
    @GetMapping(COUNTRY_PATH)
    public ResponseEntity<CommonResponse> getAllCountriesByUser() {
        log.info("Get all countries");
        var response = locationService.getAllCountriesByUser();
        return ResponseEntity.ok(new CommonResponse(SuccessCode.SUCCESS, response));
    }

    // ------------------- CITY -------------------
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

    // ------------------- DISTRICT -------------------
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

    // ------------------- WARD -------------------
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

    // ------------------- ADDRESS -------------------
    @PostMapping(ADDRESS_PATH)
    public ResponseEntity<CommonResponse> createAddressByUser(@RequestBody CreateAddressRequest request) {
        log.info("Create address with request: {}", request);
        var response = locationService.createAddressByUser(request);
        return ResponseEntity.ok(new CommonResponse(SuccessCode.SUCCESS, response));
    }

    @GetMapping(ADDRESS_PATH)
    public ResponseEntity<CommonResponse> getAddressesByUser(GetListRequest request) {
        log.info("Get addresses with request: {}", request);
        var response = locationService.getAddressesByUser(request);
        return ResponseEntity.ok(new CommonResponse(SuccessCode.SUCCESS, response));
    }

    @PatchMapping(ADDRESS_PATH + "/{addressId}")
    public ResponseEntity<CommonResponse> updateAddressByUser(@PathVariable String addressId, @RequestBody UpdateAddressRequest request) {
        log.info("Update address with id: {} and request: {}", addressId, request);
        var response = locationService.updateAddressByUser(addressId, request);
        return ResponseEntity.ok(new CommonResponse(SuccessCode.SUCCESS, response));
    }

    @DeleteMapping(ADDRESS_PATH + "/{addressId}")
    public ResponseEntity<CommonResponse> deleteAddressByUser(@PathVariable String addressId) {
        log.info("Delete address with id: {}", addressId);
        locationService.deleteAddressByUser(addressId);
        return ResponseEntity.ok(new CommonResponse(SuccessCode.SUCCESS));
    }

    @GetMapping(ADDRESS_PATH + "/{addressId}")
    public ResponseEntity<CommonResponse> getAddressByUser(@PathVariable String addressId) {
        log.info("Get address with id: {}", addressId);
        var response = locationService.getAddressByUser(addressId);
        return ResponseEntity.ok(new CommonResponse(SuccessCode.SUCCESS, response));
    }
}
