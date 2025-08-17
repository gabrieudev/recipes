package br.com.gabrieudev.recipes.application.ports.output;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.gabrieudev.recipes.domain.Recipe;


public interface RecipeOutputPort {
    Optional<Recipe> create(Recipe recipe);
    Optional<Recipe> update(Recipe recipe);
    boolean delete(UUID id);
    Optional<Recipe> findById(UUID id);
    List<Recipe> findAll(UUID userId, String title, Integer cookTimeMinutes, Integer servings, UUID categoryId);
    boolean existsById(UUID id);
    boolean existsByCategoryId(UUID id);
}
