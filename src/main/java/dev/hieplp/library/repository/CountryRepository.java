package dev.hieplp.library.repository;

import dev.hieplp.library.common.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryRepository extends JpaRepository<Country, String>, JpaSpecificationExecutor<Country> {
    boolean existsByCountryId(String countryId);

    List<Country> findAllByStatus(Byte status);
}
