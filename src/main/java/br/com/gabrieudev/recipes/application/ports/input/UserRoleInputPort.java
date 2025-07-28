package br.com.gabrieudev.recipes.application.ports.input;

import java.util.UUID;

import br.com.gabrieudev.recipes.domain.UserRole;

public interface UserRoleInputPort {
    UserRole assign(UUID userId, UUID roleId);
    void unassign(UUID userId, UUID roleId);
}
