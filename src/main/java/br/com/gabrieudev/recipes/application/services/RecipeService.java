package br.com.gabrieudev.recipes.application.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import br.com.gabrieudev.recipes.application.exceptions.AlreadyExistsException;
import br.com.gabrieudev.recipes.application.exceptions.InternalErrorException;
import br.com.gabrieudev.recipes.application.exceptions.NotFoundException;
import br.com.gabrieudev.recipes.application.ports.input.RecipeinputPort;
import br.com.gabrieudev.recipes.application.ports.output.RecipeIngredientOutputPort;
import br.com.gabrieudev.recipes.application.ports.output.RecipeOutputPort;
import br.com.gabrieudev.recipes.domain.Recipe;
import br.com.gabrieudev.recipes.domain.RecipeIngredient;

public class RecipeService implements RecipeinputPort {
    private final RecipeOutputPort recipeOutputPort;
    private final RecipeIngredientOutputPort recipeIngredientOutputPort;

    public RecipeService(RecipeOutputPort recipeOutputPort, RecipeIngredientOutputPort recipeIngredientOutputPort) {
        this.recipeOutputPort = recipeOutputPort;
        this.recipeIngredientOutputPort = recipeIngredientOutputPort;
    }

    @Override
    public Recipe create(Recipe recipe, List<RecipeIngredient> recipeIngredients) {
        if (recipeOutputPort.existsByTitle(recipe.getTitle())) {
            throw new AlreadyExistsException("Já existe uma receita com esse título.");
        }

        recipe.setCreatedAt(LocalDateTime.now());

        Recipe createdRecipe = recipeOutputPort.create(recipe)
            .orElseThrow(() -> new InternalErrorException("Erro ao criar receita."));

        recipeIngredients.forEach(ri -> {
            ri.setRecipe(createdRecipe);
            recipeIngredientOutputPort.create(ri)
                .orElseThrow(() -> new InternalErrorException("Erro ao criar ingrediente para receita."));
        });

        return createdRecipe;
    }

    @Override
    public void delete(UUID id) {
        if (!recipeOutputPort.existsById(id)) {
            throw new NotFoundException("Receita não encontrada.");
        }

        if (!recipeOutputPort.delete(id)) {
            throw new InternalErrorException("Erro ao deletar receita.");
        }
    }

    @Override
    public List<Recipe> findAll(String title, Integer cookTimeMinutes, Integer servings, UUID categoryId, Integer page, Integer size) {
        return recipeOutputPort.findAll(title, cookTimeMinutes, servings, categoryId, page, size);
    }

    @Override
    public Recipe findById(UUID id) {
        return recipeOutputPort.findById(id)
            .orElseThrow(() -> new NotFoundException("Receita não encontrada."));
    }

    @Override
    public Recipe update(Recipe recipe) {
        Recipe recipeToUpdate = recipeOutputPort.findById(recipe.getId())
            .orElseThrow(() -> new NotFoundException("Receita não encontrada."));

        if (!recipeToUpdate.getTitle().equals(recipe.getTitle()) && recipeOutputPort.existsByTitle(recipe.getTitle())) {
            throw new AlreadyExistsException("Já existe uma receita com esse título.");
        }

        return recipeOutputPort.update(recipe)
            .orElseThrow(() -> new InternalErrorException("Erro ao atualizar receita."));
    }
}
