package dev.hieplp.library.repository;

import dev.hieplp.library.common.entity.City;
import dev.hieplp.library.common.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, String>, JpaSpecificationExecutor<City> {
    boolean existsByCityId(String cityId);

    List<City> findAllByStatus(Byte status);

    List<City> findAllByStatusAndCountry(Byte status, Country country);
}
