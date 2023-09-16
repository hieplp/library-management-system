package dev.hieplp.library.service;

import dev.hieplp.library.common.payload.request.GetListRequest;
import dev.hieplp.library.common.payload.response.GetListResponse;
import dev.hieplp.library.payload.request.catalog.CreateCatalogRequest;
import dev.hieplp.library.payload.request.catalog.UpdateCatalogRequest;
import dev.hieplp.library.payload.response.catalog.AdminCatalogResponse;
import dev.hieplp.library.payload.response.catalog.CommonCatalogResponse;

public interface CatalogService {
    AdminCatalogResponse createCatalogByAdmin(CreateCatalogRequest request);

    AdminCatalogResponse updateCatalogByAdmin(String catalogId, UpdateCatalogRequest request);

    AdminCatalogResponse getCatalogByAdmin(String catalogId);

    GetListResponse<AdminCatalogResponse> getCatalogsByAdmin(GetListRequest request);

    GetListResponse<CommonCatalogResponse> getActiveCatalogs(GetListRequest request);

    CommonCatalogResponse getActiveCatalog(String catalogId);
}
