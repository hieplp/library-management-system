package dev.hieplp.library.repository;

import dev.hieplp.library.common.entity.UserRole;
import dev.hieplp.library.common.entity.key.UserRoleKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleKey> {
    @Query("SELECT ur FROM UserRole ur WHERE ur.id.userId = :userId")
    List<UserRole> getRolesByUserId(String userId);
}
