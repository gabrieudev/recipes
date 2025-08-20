package br.com.gabrieudev.recipes.adapters.output.persistence.repositories.jpa;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.gabrieudev.recipes.adapters.output.persistence.entities.JpaIngredientEntity;
import br.com.gabrieudev.recipes.adapters.output.persistence.entities.JpaRecipeEntity;
import br.com.gabrieudev.recipes.adapters.output.persistence.entities.JpaRecipeIngredientEntity;

@Repository
public interface JpaRecipeIngredientRepository extends JpaRepository<JpaRecipeIngredientEntity, UUID> {
    boolean existsByRecipeAndIngredient(JpaRecipeEntity recipe, JpaIngredientEntity ingredient);

    @Query(
        value = """
                SELECT ri.*
                FROM recipe_ingredient ri
                WHERE 
                    (:p1 IS NULL OR ri.recipe_id = :p1) 
                AND
                    (:p2 IS NULL OR ri.ingredient_id = :p2)
                """,
        nativeQuery = true
    )
    Page<JpaRecipeIngredientEntity> findAll(@Param("p1") UUID recipeId, @Param("p2") UUID ingredientId, Pageable pageable);
}
