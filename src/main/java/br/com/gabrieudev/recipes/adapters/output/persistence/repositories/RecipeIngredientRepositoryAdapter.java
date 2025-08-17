package br.com.gabrieudev.recipes.adapters.output.persistence.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import br.com.gabrieudev.recipes.adapters.output.persistence.entities.JpaIngredientEntity;
import br.com.gabrieudev.recipes.adapters.output.persistence.entities.JpaRecipeEntity;
import br.com.gabrieudev.recipes.adapters.output.persistence.entities.JpaRecipeIngredientEntity;
import br.com.gabrieudev.recipes.adapters.output.persistence.repositories.jpa.JpaIngredientRepository;
import br.com.gabrieudev.recipes.adapters.output.persistence.repositories.jpa.JpaRecipeIngredientRepository;
import br.com.gabrieudev.recipes.adapters.output.persistence.repositories.jpa.JpaRecipeRepository;
import br.com.gabrieudev.recipes.application.ports.output.RecipeIngredientOutputPort;
import br.com.gabrieudev.recipes.domain.RecipeIngredient;

@Component
public class RecipeIngredientRepositoryAdapter implements RecipeIngredientOutputPort {
    private final JpaRecipeIngredientRepository jpaRecipeIngredientRepository;
    private final JpaRecipeRepository jpaRecipeRepository;
    private final JpaIngredientRepository jpaIngredientRepository;

    public RecipeIngredientRepositoryAdapter(JpaRecipeIngredientRepository jpaRecipeIngredientRepository,
            JpaRecipeRepository jpaRecipeRepository, JpaIngredientRepository jpaIngredientRepository) {
        this.jpaRecipeIngredientRepository = jpaRecipeIngredientRepository;
        this.jpaRecipeRepository = jpaRecipeRepository;
        this.jpaIngredientRepository = jpaIngredientRepository;
    }

    @Override
    public Optional<RecipeIngredient> create(RecipeIngredient recipeIngredient) {
        try {
            JpaRecipeIngredientEntity savedRecipeIngredient = jpaRecipeIngredientRepository.save(JpaRecipeIngredientEntity.fromDomainObj(recipeIngredient));

            return Optional.of(savedRecipeIngredient.toDomainObj());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean delete(RecipeIngredient recipeIngredient) {
        try {
            jpaRecipeIngredientRepository.delete(JpaRecipeIngredientEntity.fromDomainObj(recipeIngredient));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaRecipeIngredientRepository.existsById(id);
    }

    @Override
    public boolean existsByRecipeIdAndIngredientId(UUID recipeId, UUID ingredientId) {
        try {
            JpaRecipeEntity recipe = jpaRecipeRepository.findById(recipeId).get();
            JpaIngredientEntity ingredient = jpaIngredientRepository.findById(ingredientId).get();

            return jpaRecipeIngredientRepository.existsByRecipeAndIngredient(recipe, ingredient);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Optional<RecipeIngredient> findById(UUID id) {
        return jpaRecipeIngredientRepository.findById(id)
                .map(JpaRecipeIngredientEntity::toDomainObj);
    }

    @Override
    public Optional<RecipeIngredient> update(RecipeIngredient recipeIngredient) {
        try {
            JpaRecipeIngredientEntity recipeIngredientToUpdate = jpaRecipeIngredientRepository.findById(recipeIngredient.getId()).get();

            recipeIngredientToUpdate.update(recipeIngredient);

            return Optional.of(jpaRecipeIngredientRepository.save(recipeIngredientToUpdate).toDomainObj());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
