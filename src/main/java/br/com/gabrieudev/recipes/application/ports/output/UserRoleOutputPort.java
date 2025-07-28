package br.com.gabrieudev.recipes.application.ports.output;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.gabrieudev.recipes.domain.UserRole;

public interface UserRoleOutputPort {
    Optional<UserRole> create(UserRole userRole);
    boolean delete(UUID id);
    List<UserRole> findByUserId(UUID userId);
    Optional<UserRole> findByUserIdAndRoleId(UUID userId, UUID roleId);
    boolean existsById(UUID id);
    boolean existsByUserIdAndRoleId(UUID userId, UUID roleId);
    boolean existsByRoleId(UUID roleId);
    boolean deleteByUserIdAndRoleId(UUID userId, UUID roleId);
}
