package dev.hieplp.library.service.impl;

import dev.hieplp.library.common.helper.RoleHelper;
import dev.hieplp.library.repository.UserRoleRepository;
import dev.hieplp.library.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository userRoleRepo;

    private final RoleHelper roleHelper;

    @Override
    public Set<Byte> getAllRoles(String userId) {
        log.info("Get all roles of user with userId: {}", userId);
        return roleHelper.getRolesByUserId(userId);
    }
}
