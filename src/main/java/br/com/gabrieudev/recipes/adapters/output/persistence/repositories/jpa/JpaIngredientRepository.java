package br.com.gabrieudev.recipes.adapters.output.persistence.repositories.jpa;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.gabrieudev.recipes.adapters.output.persistence.entities.JpaIngredientEntity;

@Repository
public interface JpaIngredientRepository extends JpaRepository<JpaIngredientEntity, UUID> {
    @Query(
        value = """
                SELECT i.*
                FROM ingredients i
                WHERE 
                    (:p1 IS NULL OR LOWER(i.name) LIKE CONCAT('%', LOWER(:p1), '%'))
                """,
        nativeQuery = true
    )
    Page<JpaIngredientEntity> findAll(@Param("p1") String name, Pageable pageable);
}
