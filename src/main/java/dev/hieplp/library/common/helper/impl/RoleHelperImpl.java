package dev.hieplp.library.common.helper.impl;

import dev.hieplp.library.common.helper.RoleHelper;
import dev.hieplp.library.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoleHelperImpl implements RoleHelper {

    private final UserRoleRepository userRoleRepo;

    @Override
    public Set<Byte> getRolesByUserId(String userId) {
        log.info("Get all roles of user: {}", userId);
        var userRoles = userRoleRepo.getRolesByUserId(userId);
        return userRoles.stream()
                .map(userRole -> userRole.getId().getRole())
                .collect(Collectors.toSet());
    }
}
