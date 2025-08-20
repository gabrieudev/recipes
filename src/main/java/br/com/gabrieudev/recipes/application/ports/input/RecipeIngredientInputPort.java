package br.com.gabrieudev.recipes.application.ports.input;

import java.util.UUID;

import br.com.gabrieudev.recipes.domain.RecipeIngredient;

public interface RecipeIngredientInputPort {
    RecipeIngredient create(RecipeIngredient recipeIngredient);
    RecipeIngredient update(RecipeIngredient recipeIngredient);
    void delete(RecipeIngredient recipeIngredient);
    RecipeIngredient findById(UUID id);
}
