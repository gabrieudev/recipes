package br.com.gabrieudev.recipes.application.ports.input;

import java.util.List;
import java.util.UUID;

import br.com.gabrieudev.recipes.domain.Recipe;

public interface RecipeinputPort {
    Recipe create(Recipe recipe);
    Recipe update(Recipe recipe);
    void delete(UUID id);
    Recipe findById(UUID id);
    List<Recipe> findAll(UUID userId, String title, Integer cookTimeMinutes, Integer servings, UUID categoryId);
}
