package dev.hieplp.library.controller.admin;

import dev.hieplp.library.common.enums.response.SuccessCode;
import dev.hieplp.library.common.payload.request.GetListRequest;
import dev.hieplp.library.common.payload.response.CommonResponse;
import dev.hieplp.library.payload.request.catalog.CreateCatalogRequest;
import dev.hieplp.library.payload.request.catalog.UpdateCatalogRequest;
import dev.hieplp.library.service.CatalogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin/catalogs")
@RequiredArgsConstructor
public class AdminCatalogController {

    private final CatalogService catalogService;

    @PostMapping
    public ResponseEntity<CommonResponse> createCatalogByAdmin(@Valid @RequestBody CreateCatalogRequest request) {
        log.info("Create catalog with request: {}", request);
        var response = catalogService.createCatalogByAdmin(request);
        return ResponseEntity.ok(new CommonResponse(SuccessCode.SUCCESS, response));
    }

    @GetMapping
    public ResponseEntity<CommonResponse> getCatalogsByAdmin(GetListRequest request) {
        log.info("Get catalogs by admin with request: {}", request);
        var response = catalogService.getCatalogsByAdmin(request);
        return ResponseEntity.ok(new CommonResponse(SuccessCode.SUCCESS, response));
    }

    @GetMapping("/{catalogId}")
    public ResponseEntity<CommonResponse> getCatalogByAdmin(@PathVariable String catalogId) {
        log.info("Get catalog by admin with catalogId: {}", catalogId);
        var response = catalogService.getCatalogByAdmin(catalogId);
        return ResponseEntity.ok(new CommonResponse(SuccessCode.SUCCESS, response));
    }

    @PatchMapping("/{catalogId}")
    public ResponseEntity<CommonResponse> updateCatalogByAdmin(@PathVariable String catalogId,
                                                               @Valid @RequestBody UpdateCatalogRequest request) {
        log.info("Update catalog by admin with request: {}", request);
        var response = catalogService.updateCatalogByAdmin(catalogId, request);
        return ResponseEntity.ok(new CommonResponse(SuccessCode.SUCCESS, response));
    }
}
