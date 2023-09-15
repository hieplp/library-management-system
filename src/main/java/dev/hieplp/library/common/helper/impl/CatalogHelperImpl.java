package dev.hieplp.library.common.helper.impl;

import dev.hieplp.library.common.entity.Catalog;
import dev.hieplp.library.common.enums.catalog.CatalogStatus;
import dev.hieplp.library.common.exception.NotFoundException;
import dev.hieplp.library.common.helper.CatalogHelper;
import dev.hieplp.library.repository.CatalogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CatalogHelperImpl implements CatalogHelper {

    private final CatalogRepository catalogRepo;

    @Override
    public Catalog getCatalog(String catalogId) {
        log.info("Get catalog with catalogId: {}", catalogId);
        return catalogRepo.findById(catalogId)
                .orElseThrow(() -> {
                    var message = String.format("Catalog with catalogId: %s not found", catalogId);
                    log.warn(message);
                    return new NotFoundException(message);
                });
    }

    @Override
    public Catalog getActiveCatalog(String catalogId) {
        log.info("Get active catalog with catalogId: {}", catalogId);
        var catalog = getCatalog(catalogId);
        if (CatalogStatus.ACTIVE.getStatus().equals(catalog.getStatus())) {
            var message = String.format("Catalog with catalogId: %s is inactive", catalogId);
            log.warn(message);
            throw new NotFoundException(message);
        }
        return catalog;
    }
}
