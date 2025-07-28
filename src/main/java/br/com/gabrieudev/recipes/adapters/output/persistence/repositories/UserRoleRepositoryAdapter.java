package br.com.gabrieudev.recipes.adapters.output.persistence.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import br.com.gabrieudev.recipes.adapters.output.persistence.entities.JpaRoleEntity;
import br.com.gabrieudev.recipes.adapters.output.persistence.entities.JpaUserEntity;
import br.com.gabrieudev.recipes.adapters.output.persistence.entities.JpaUserRoleEntity;
import br.com.gabrieudev.recipes.adapters.output.persistence.repositories.jpa.JpaRoleRepository;
import br.com.gabrieudev.recipes.adapters.output.persistence.repositories.jpa.JpaUserRepository;
import br.com.gabrieudev.recipes.adapters.output.persistence.repositories.jpa.JpaUserRoleRepository;
import br.com.gabrieudev.recipes.application.ports.output.UserRoleOutputPort;
import br.com.gabrieudev.recipes.domain.UserRole;

@Component
public class UserRoleRepositoryAdapter implements UserRoleOutputPort {
    private final JpaUserRoleRepository jpaUserRoleRepository;
    private final JpaUserRepository jpaUserRepository;
    private final JpaRoleRepository jpaRoleRepository;

    public UserRoleRepositoryAdapter(JpaUserRoleRepository jpaUserRoleRepository, JpaUserRepository jpaUserRepository,
            JpaRoleRepository jpaRoleRepository) {
        this.jpaUserRoleRepository = jpaUserRoleRepository;
        this.jpaUserRepository = jpaUserRepository;
        this.jpaRoleRepository = jpaRoleRepository;
    }

    @Override
    public Optional<UserRole> create(UserRole userRole) {
        try {
            JpaUserRoleEntity savedUserRole = jpaUserRoleRepository.save(JpaUserRoleEntity.from(userRole));

            return Optional.of(savedUserRole.toDomainObj());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean deleteByUserIdAndRoleId(UUID userId, UUID roleId) {
        try {
            JpaUserEntity user = jpaUserRepository.findById(userId).orElse(null);

            JpaRoleEntity role = jpaRoleRepository.findById(roleId).orElse(null);

            if (user == null || role == null) {
                return false;
            }

            jpaUserRoleRepository.deleteByUserAndRole(user, role);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean existsByRoleId(UUID roleId) {
        return jpaRoleRepository.existsById(roleId);
    }

    @Override
    public boolean existsByUserIdAndRoleId(UUID userId, UUID roleId) {
        try {
            JpaUserEntity user = jpaUserRepository.findById(userId).orElse(null);

            JpaRoleEntity role = jpaRoleRepository.findById(roleId).orElse(null);

            if (user == null || role == null) {
                return false;
            }

            return jpaUserRoleRepository.existsByUserAndRole(user, role);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean delete(UUID id) {
        try {
            jpaUserRoleRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaUserRoleRepository.existsById(id);
    }

    @Override
    public Optional<UserRole> findByUserIdAndRoleId(UUID userId, UUID roleId) {
        JpaUserEntity user = jpaUserRepository.findById(userId).orElse(null);

        JpaRoleEntity role = jpaRoleRepository.findById(roleId).orElse(null);

        if (user == null || role == null) {
            return Optional.empty();
        }

        return jpaUserRoleRepository.findByUserAndRole(user, role)
            .map(JpaUserRoleEntity::toDomainObj);
    }

    @Override
    public List<UserRole> findByUserId(UUID userId) {
        JpaUserEntity user = jpaUserRepository.findById(userId).orElse(null);

        return jpaUserRoleRepository.findByUser(user).stream()
            .map(JpaUserRoleEntity::toDomainObj)
            .toList();
    }
}
