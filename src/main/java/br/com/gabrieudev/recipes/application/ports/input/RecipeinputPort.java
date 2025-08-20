package br.com.gabrieudev.recipes.application.ports.input;

import java.util.List;
import java.util.UUID;

import br.com.gabrieudev.recipes.domain.Recipe;
import br.com.gabrieudev.recipes.domain.RecipeIngredient;

public interface RecipeinputPort {
    Recipe create(Recipe recipe, List<RecipeIngredient> recipeIngredients);
    Recipe update(Recipe recipe);
    void delete(UUID id);
    Recipe findById(UUID id);
    List<Recipe> findAll(String title, Integer cookTimeMinutes, Integer servings, UUID categoryId, Integer page, Integer size);
}
