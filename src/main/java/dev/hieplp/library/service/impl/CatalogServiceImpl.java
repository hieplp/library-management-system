package dev.hieplp.library.service.impl;

import dev.hieplp.library.common.entity.Catalog;
import dev.hieplp.library.common.enums.IdLength;
import dev.hieplp.library.common.enums.catalog.CatalogStatus;
import dev.hieplp.library.common.helper.CatalogHelper;
import dev.hieplp.library.common.payload.request.GetListRequest;
import dev.hieplp.library.common.payload.response.GetListResponse;
import dev.hieplp.library.common.util.DateTimeUtil;
import dev.hieplp.library.common.util.GeneratorUtil;
import dev.hieplp.library.common.util.SqlUtil;
import dev.hieplp.library.config.security.CurrentUser;
import dev.hieplp.library.payload.request.catalog.CreateCatalogRequest;
import dev.hieplp.library.payload.request.catalog.UpdateCatalogRequest;
import dev.hieplp.library.payload.response.catalog.AdminCatalogResponse;
import dev.hieplp.library.payload.response.catalog.CommonCatalogResponse;
import dev.hieplp.library.repository.CatalogRepository;
import dev.hieplp.library.service.CatalogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CatalogServiceImpl implements CatalogService {

    private final CurrentUser currentUser;

    private final CatalogRepository catalogRepo;

    private final CatalogHelper catalogHelper;

    private final DateTimeUtil dateTimeUtil;
    private final GeneratorUtil generatorUtil;
    private final SqlUtil sqlUtil;

    @Override
    public AdminCatalogResponse createCatalogByAdmin(CreateCatalogRequest request) {
        log.info("Create catalog with request: {}", request);

        final var catalogId = generatorUtil.generateId(IdLength.CATALOG_ID);
        var catalog = Catalog.builder()
                .catalogId(catalogId)
                .catalogName(request.getCatalogName())
                .description(request.getDescription())
                .status(CatalogStatus.ACTIVE.getStatus())
                .createdBy(currentUser.getUserId())
                .createdAt(dateTimeUtil.getCurrentTimestamp())
                .modifiedBy(currentUser.getUserId())
                .modifiedAt(dateTimeUtil.getCurrentTimestamp())
                .build();

        if (request.getParentCatalogId() != null) {
            var parentCatalog = catalogHelper.getCatalog(request.getParentCatalogId());
            catalog.setParentCatalog(parentCatalog);
        }

        catalogRepo.save(catalog);

        var response = new AdminCatalogResponse();
        BeanUtils.copyProperties(catalog, response);
        response.setParentCatalog(catalog.getParentCatalog().getCatalogId());
        return response;
    }

    @Override
    public AdminCatalogResponse updateCatalogByAdmin(String catalogId, UpdateCatalogRequest request) {
        log.info("Update catalog with catalogId: {} and request: {}", catalogId, request);

        var catalog = catalogHelper.getCatalog(catalogId);
        var isChanged = false;

        if (request.getCatalogName() != null && !request.getCatalogName().equals(catalog.getCatalogName())) {
            catalog.setCatalogName(request.getCatalogName());
            isChanged = true;
        }

        if (request.getDescription() != null && !request.getDescription().equals(catalog.getDescription())) {
            catalog.setDescription(request.getDescription());
            isChanged = true;
        }

        if (request.getStatus() != null && !request.getStatus().equals(catalog.getStatus())) {
            var status = CatalogStatus.fromStatus(request.getStatus());
            catalog.setStatus(status.getStatus());
            isChanged = true;
        }

        if (request.getParentCatalogId() != null) {
            var parentCatalog = catalogHelper.getActiveCatalog(request.getParentCatalogId());
            catalog.setParentCatalog(parentCatalog);
            isChanged = true;
        }

        if (isChanged) {
            catalog
                    .setModifiedBy(currentUser.getUserId())
                    .setModifiedAt(dateTimeUtil.getCurrentTimestamp());
            catalogRepo.save(catalog);
        }

        var response = new AdminCatalogResponse();
        BeanUtils.copyProperties(catalog, response);
        return response;
    }

    @Override
    public AdminCatalogResponse getCatalogByAdmin(String catalogId) {
        log.info("Get catalog with catalogId: {}", catalogId);
        var catalog = catalogHelper.getCatalog(catalogId);
        var response = new AdminCatalogResponse();
        BeanUtils.copyProperties(catalog, response);
        return response;
    }

    @Override
    public GetListResponse<AdminCatalogResponse> getCatalogsByAdmin(GetListRequest request) {
        log.info("Get catalogs with request: {}", request);
        return sqlUtil.getList(request, catalogRepo, AdminCatalogResponse.class);
    }

    @Override
    public GetListResponse<CommonCatalogResponse> getActiveCatalogs(GetListRequest request) {
        log.info("Get catalogs with request: {}", request);
        request.setStatuses(new Byte[]{CatalogStatus.ACTIVE.getStatus()});
        return sqlUtil.getList(request, catalogRepo, CommonCatalogResponse.class);
    }

    @Override
    public CommonCatalogResponse getActiveCatalog(String catalogId) {
        log.info("Get catalog with catalogId: {}", catalogId);
        var catalog = catalogHelper.getActiveCatalog(catalogId);
        var response = new CommonCatalogResponse();
        BeanUtils.copyProperties(catalog, response);
        return response;
    }
}
