package dev.hieplp.library.service;

import java.util.Set;

public interface UserRoleService {
    Set<Byte> getAllRoles(String userId);
}
