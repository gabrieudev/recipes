package br.com.gabrieudev.recipes.adapters.output.persistence.repositories.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.gabrieudev.recipes.adapters.output.persistence.entities.JpaRoleEntity;
import br.com.gabrieudev.recipes.adapters.output.persistence.entities.JpaUserEntity;
import br.com.gabrieudev.recipes.adapters.output.persistence.entities.JpaUserRoleEntity;

@Repository
public interface JpaUserRoleRepository extends JpaRepository<JpaUserRoleEntity, UUID> {
    List<JpaUserRoleEntity> findByUser(JpaUserEntity user);
    boolean existsByUserAndRole(JpaUserEntity user, JpaRoleEntity role);
    boolean existsByRole(JpaRoleEntity role);
    void deleteByUserAndRole(JpaUserEntity user, JpaRoleEntity role);
    Optional<JpaUserRoleEntity> findByUserAndRole(JpaUserEntity user, JpaRoleEntity role);
}
