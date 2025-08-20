package br.com.gabrieudev.recipes.adapters.output.persistence.repositories.jpa;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.gabrieudev.recipes.adapters.output.persistence.entities.JpaCategoryEntity;
import br.com.gabrieudev.recipes.adapters.output.persistence.entities.JpaRecipeEntity;

@Repository
public interface JpaRecipeRepository extends JpaRepository<JpaRecipeEntity, UUID> {
    @Query(
        value = """
                SELECT r.*
                FROM recipes r
                WHERE
                    (:p1 IS NULL OR r.category_id = :p1)
                AND
                    (:p2 IS NULL OR LOWER(r.title) LIKE CONCAT('%', LOWER(:p2), '%'))
                AND
                    (:p3 IS NULL OR r.cook_time_minutes = :p3)
                AND
                    (:p4 IS NULL OR r.servings = :p4)
                """,
        nativeQuery = true
    )
    Page<JpaRecipeEntity> findAll(
        @Param("p1") UUID categoryId, 
        @Param("p2") String title, 
        @Param("p3") Integer cookTimeMinutes, 
        @Param("p4") Integer servings, 
        Pageable pageable
    );

    boolean existsByCategory(JpaCategoryEntity category);

    boolean existsByTitle(String title);
}
