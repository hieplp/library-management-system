package dev.hieplp.library.repository;

import dev.hieplp.library.common.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, String>, JpaSpecificationExecutor<City> {
    boolean existsByCityId(String cityId);
}
