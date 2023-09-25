package dev.hieplp.library.repository;

import dev.hieplp.library.common.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DistrictRepository extends JpaRepository<District, String>, JpaSpecificationExecutor<District> {
    boolean existsByDistrictId(String cityId);
}
