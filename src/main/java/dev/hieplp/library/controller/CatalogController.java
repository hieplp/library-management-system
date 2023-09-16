package dev.hieplp.library.controller;

import dev.hieplp.library.common.enums.response.SuccessCode;
import dev.hieplp.library.common.payload.request.GetListRequest;
import dev.hieplp.library.common.payload.response.CommonResponse;
import dev.hieplp.library.service.CatalogService;
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
@RequestMapping("/catalogs")
public class CatalogController {

    private final CatalogService catalogService;

    @GetMapping
    public ResponseEntity<CommonResponse> getActiveCatalogs(GetListRequest request) {
        log.info("Get active catalogs with request: {}", request);
        var response = catalogService.getActiveCatalogs(request);
        return ResponseEntity.ok(new CommonResponse(SuccessCode.SUCCESS, response));
    }

    @GetMapping("/{catalogId}")
    public ResponseEntity<CommonResponse> getActiveCatalog(@PathVariable String catalogId) {
        log.info("Get active catalog with catalogId: {}", catalogId);
        var response = catalogService.getActiveCatalog(catalogId);
        return ResponseEntity.ok(new CommonResponse(SuccessCode.SUCCESS, response));
    }
}
