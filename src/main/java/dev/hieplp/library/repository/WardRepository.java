package dev.hieplp.library.repository;

import dev.hieplp.library.common.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface WardRepository extends JpaRepository<Ward, String>, JpaSpecificationExecutor<Ward> {
    boolean existsByWardId(String wardId);
}
