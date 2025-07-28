package br.com.gabrieudev.recipes.adapters.output.persistence.repositories.jpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.gabrieudev.recipes.adapters.output.persistence.entities.JpaUserEntity;

@Repository
public interface JpaUserRepository extends JpaRepository<JpaUserEntity, UUID> {
    boolean existsByEmail(String email);

    Optional<JpaUserEntity> findByEmail(String email);

    @Query(value = """
            SELECT u.*
            FROM users u
            WHERE 
                ((:p1 IS NULL) OR (LOWER(u.first_name) LIKE CONCAT('%', LOWER(:p1), '%') OR LOWER(u.last_name) LIKE CONCAT('%', LOWER(:p1), '%')))
            AND 
                (:p2 IS NULL OR u.email = :p2)
            """, nativeQuery = true)
    Page<JpaUserEntity> findAll(
            @Param("p1") String param,
            @Param("p2") String email,
            Pageable pageable);
}
