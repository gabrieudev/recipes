package br.com.gabrieudev.recipes.adapters.output.persistence.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import br.com.gabrieudev.recipes.adapters.output.persistence.entities.JpaRoleEntity;
import br.com.gabrieudev.recipes.adapters.output.persistence.repositories.jpa.JpaRoleRepository;
import br.com.gabrieudev.recipes.application.ports.output.RoleOutputPort;
import br.com.gabrieudev.recipes.domain.Role;

@Component
public class RoleRepositoryAdapter implements RoleOutputPort {
    private final JpaRoleRepository jpaRoleRepository;

    public RoleRepositoryAdapter(JpaRoleRepository jpaRoleRepository) {
        this.jpaRoleRepository = jpaRoleRepository;
    }

    @Override
    public Optional<Role> create(Role role) {
        try {
            JpaRoleEntity savedRole = jpaRoleRepository.save(JpaRoleEntity.from(role));

            return Optional.of(savedRole.toDomainObj());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean delete(UUID id) {
        try {
            jpaRoleRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaRoleRepository.existsById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRoleRepository.existsByName(name);
    }

    @Override
    public List<Role> findAll(UUID userId, String name) {
        return jpaRoleRepository.findAll(userId, name)
            .stream()
            .map(JpaRoleEntity::toDomainObj)
            .toList();
    }

    @Override
    public Optional<Role> findById(UUID id) {
        return jpaRoleRepository.findById(id)
            .map(JpaRoleEntity::toDomainObj);
    }

    @Override
    public Optional<Role> findByName(String name) {
        return jpaRoleRepository.findByName(name)
            .map(JpaRoleEntity::toDomainObj);
    }

    @Override
    public Optional<Role> update(Role role) {
        try {
            JpaRoleEntity roleToUpdate = jpaRoleRepository.findById(role.getId()).orElse(null);
            
            if (roleToUpdate == null) {
                return Optional.empty();
            }

            roleToUpdate.update(role);

            return Optional.of(jpaRoleRepository.save(roleToUpdate).toDomainObj());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
