package br.com.gabrieudev.recipes.application.ports.output;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.gabrieudev.recipes.domain.Role;

public interface RoleOutputPort {
    Optional<Role> create(Role role);
    Optional<Role> update(Role role);
    boolean delete(UUID id);
    Optional<Role> findById(UUID id);
    Optional<Role> findByName(String name);
    boolean existsById(UUID id);
    boolean existsByName(String name);
    List<Role> findAll(UUID userId, String name);
}
