package dev.hieplp.library.common.helper;

import dev.hieplp.library.common.entity.Catalog;

public interface CatalogHelper {
    Catalog getCatalog(String catalogId);

    Catalog getActiveCatalog(String catalogId);

}
