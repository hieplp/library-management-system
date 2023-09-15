package dev.hieplp.library.repository;

import dev.hieplp.library.common.entity.Catalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CatalogRepository extends JpaRepository<Catalog, String>, JpaSpecificationExecutor<Catalog> {
}
