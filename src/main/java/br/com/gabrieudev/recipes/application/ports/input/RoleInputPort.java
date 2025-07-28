package br.com.gabrieudev.recipes.application.ports.input;

import java.util.List;
import java.util.UUID;

import br.com.gabrieudev.recipes.domain.Role;

public interface RoleInputPort {
    Role create(Role role);
    Role update(Role role);
    void delete(UUID id);
    Role findById(UUID id);
    List<Role> findAll(UUID userId, String name);
}
