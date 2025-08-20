package br.com.gabrieudev.recipes.application.ports.input;

import java.util.List;
import java.util.UUID;

import br.com.gabrieudev.recipes.domain.RecipeIngredient;

public interface RecipeIngredientInputPort {
    RecipeIngredient create(RecipeIngredient recipeIngredient);
    RecipeIngredient update(RecipeIngredient recipeIngredient);
    void delete(UUID id);
    RecipeIngredient findById(UUID id);
    List<RecipeIngredient> findAll(UUID recipeId, UUID ingredientId, Integer page, Integer size);
}
