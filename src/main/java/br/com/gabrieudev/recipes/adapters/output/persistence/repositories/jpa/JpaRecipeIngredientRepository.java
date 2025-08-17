package br.com.gabrieudev.recipes.adapters.output.persistence.repositories.jpa;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.gabrieudev.recipes.adapters.output.persistence.entities.JpaIngredientEntity;
import br.com.gabrieudev.recipes.adapters.output.persistence.entities.JpaRecipeEntity;
import br.com.gabrieudev.recipes.adapters.output.persistence.entities.JpaRecipeIngredientEntity;

@Repository
public interface JpaRecipeIngredientRepository extends JpaRepository<JpaRecipeIngredientEntity, UUID> {
    boolean existsByRecipeAndIngredient(JpaRecipeEntity recipe, JpaIngredientEntity ingredient);
}
