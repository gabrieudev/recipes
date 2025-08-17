package br.com.gabrieudev.recipes.adapters.output.persistence.repositories.jpa;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.gabrieudev.recipes.adapters.output.persistence.entities.JpaFavoriteEntity;

@Repository
public interface JpaFavoriteRepository extends JpaRepository<JpaFavoriteEntity, UUID> {
    @Query(
        value = """
                SELECT f.*
                FROM favorites f
                    WHERE f.user_id = :p1
                """,
        nativeQuery = true
    )
    Page<JpaFavoriteEntity> findAll(@Param("p1") UUID userId, Pageable pageable);
}
