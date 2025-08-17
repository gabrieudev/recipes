package br.com.gabrieudev.recipes.adapters.output.persistence.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import br.com.gabrieudev.recipes.adapters.output.persistence.entities.JpaCategoryEntity;
import br.com.gabrieudev.recipes.adapters.output.persistence.entities.JpaRecipeEntity;
import br.com.gabrieudev.recipes.adapters.output.persistence.repositories.jpa.JpaCategoryRepository;
import br.com.gabrieudev.recipes.adapters.output.persistence.repositories.jpa.JpaRecipeRepository;
import br.com.gabrieudev.recipes.application.ports.output.RecipeOutputPort;
import br.com.gabrieudev.recipes.domain.Recipe;

@Component
public class RecipeRepositoryAdapter implements RecipeOutputPort {
    private final JpaRecipeRepository jpaRecipeRepository;
    private final JpaCategoryRepository jpaCategoryRepository;

    public RecipeRepositoryAdapter(JpaRecipeRepository jpaRecipeRepository, JpaCategoryRepository jpaCategoryRepository) {
        this.jpaRecipeRepository = jpaRecipeRepository;
        this.jpaCategoryRepository = jpaCategoryRepository;
    }

    @Override
    public Optional<Recipe> create(Recipe recipe) {
        try {
            JpaRecipeEntity savedRecipe = jpaRecipeRepository.save(JpaRecipeEntity.fromDomainObj(recipe));

            return Optional.of(savedRecipe.toDomainObj());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean delete(UUID id) {
        try {
            jpaRecipeRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean existsByCategoryId(UUID id) {
        try {
            JpaCategoryEntity category = jpaCategoryRepository.findById(id).get();

            return jpaRecipeRepository.existsByCategory(category);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaRecipeRepository.existsById(id);
    }

    @Override
    public boolean existsByTitle(String title) {
        return jpaRecipeRepository.existsByTitle(title);
    }

    @Override
    public List<Recipe> findAll(UUID userId, String title, Integer cookTimeMinutes, Integer servings, UUID categoryId, Integer page, Integer size) {
        return jpaRecipeRepository.findAll(userId, categoryId, title, cookTimeMinutes, servings, PageRequest.of(page, size))
                .stream()
                .map(JpaRecipeEntity::toDomainObj)
                .toList();
    }

    @Override
    public Optional<Recipe> findById(UUID id) {
        return jpaRecipeRepository.findById(id)
                .map(JpaRecipeEntity::toDomainObj);
    }

    @Override
    public Optional<Recipe> update(Recipe recipe) {
        try {
            JpaRecipeEntity recipeToUpdate = jpaRecipeRepository.findById(recipe.getId())
                    .orElseThrow(() -> new RuntimeException("Receita não encontrada."));

            recipeToUpdate.update(recipe);

            return Optional.of(jpaRecipeRepository.save(recipeToUpdate).toDomainObj());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
