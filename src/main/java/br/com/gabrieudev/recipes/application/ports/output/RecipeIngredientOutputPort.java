package br.com.gabrieudev.recipes.application.ports.output;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.gabrieudev.recipes.domain.RecipeIngredient;

public interface RecipeIngredientOutputPort {
    Optional<RecipeIngredient> create(RecipeIngredient recipeIngredient);
    Optional<RecipeIngredient> update(RecipeIngredient recipeIngredient);
    boolean delete(UUID id);
    Optional<RecipeIngredient> findById(UUID id);
    boolean existsById(UUID id);
    boolean existsByRecipeIdAndIngredientId(UUID recipeId, UUID ingredientId);
    List<RecipeIngredient> findAll(UUID recipeId, UUID ingredientId, Integer page, Integer size);
}
