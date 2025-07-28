package br.com.gabrieudev.recipes.adapters.output.persistence.repositories.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.gabrieudev.recipes.adapters.output.persistence.entities.JpaRoleEntity;

@Repository
public interface JpaRoleRepository extends JpaRepository<JpaRoleEntity, UUID> {
    @Query(value = """
            SELECT r.*
            FROM roles r
            LEFT JOIN users_roles ur
                ON r.id = ur.role_id
            WHERE
                (:p1 IS NULL OR ur.user_id = :p1)
            AND
                (:p2 IS NULL OR r.name = :p2)
            """, nativeQuery = true)
    List<JpaRoleEntity> findAll(
            @Param("p1") UUID userId,
            @Param("p2") String name);

    Optional<JpaRoleEntity> findByName(String name);

    boolean existsByName(String name);
}
