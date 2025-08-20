package br.com.gabrieudev.recipes.application.services;

import java.util.UUID;

import br.com.gabrieudev.recipes.application.exceptions.AlreadyExistsException;
import br.com.gabrieudev.recipes.application.exceptions.InternalErrorException;
import br.com.gabrieudev.recipes.application.ports.input.RecipeIngredientInputPort;
import br.com.gabrieudev.recipes.application.ports.output.RecipeIngredientOutputPort;
import br.com.gabrieudev.recipes.domain.RecipeIngredient;

public class RecipeIngredientService implements RecipeIngredientInputPort {
    private final RecipeIngredientOutputPort recipeIngredientOutputPort;

    public RecipeIngredientService(RecipeIngredientOutputPort recipeIngredientOutputPort) {
        this.recipeIngredientOutputPort = recipeIngredientOutputPort;
    }

    @Override
    public RecipeIngredient create(RecipeIngredient recipeIngredient) {
        if (recipeIngredientOutputPort.existsByRecipeIdAndIngredientId(recipeIngredient.getRecipe().getId(), recipeIngredient.getIngredient().getId())) {
            throw new AlreadyExistsException("Já existe um ingrediente para essa receita.");
        }

        return recipeIngredientOutputPort.create(recipeIngredient)
                .orElseThrow(() -> new InternalErrorException("Erro ao criar ingrediente para receita."));
    }

    @Override
    public void delete(RecipeIngredient recipeIngredient) {
        if (!recipeIngredientOutputPort.existsById(recipeIngredient.getId())) {
            throw new InternalErrorException("Ingrediente para receita não encontrado.");
        }

        if (!recipeIngredientOutputPort.delete(recipeIngredient)) {
            throw new InternalErrorException("Erro ao deletar ingrediente para receita.");
        }
    }

    @Override
    public RecipeIngredient findById(UUID id) {
        return recipeIngredientOutputPort.findById(id)
                .orElseThrow(() -> new InternalErrorException("Ingrediente para receita não encontrado."));
    }

    @Override
    public RecipeIngredient update(RecipeIngredient recipeIngredient) {
        return recipeIngredientOutputPort.update(recipeIngredient)
                .orElseThrow(() -> new InternalErrorException("Erro ao atualizar ingrediente para receita."));
    }
}
