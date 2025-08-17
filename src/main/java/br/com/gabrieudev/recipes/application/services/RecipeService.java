package br.com.gabrieudev.recipes.application.services;

import java.util.List;
import java.util.UUID;

import br.com.gabrieudev.recipes.application.exceptions.AlreadyExistsException;
import br.com.gabrieudev.recipes.application.exceptions.InternalErrorException;
import br.com.gabrieudev.recipes.application.exceptions.NotFoundException;
import br.com.gabrieudev.recipes.application.ports.input.RecipeinputPort;
import br.com.gabrieudev.recipes.application.ports.output.RecipeOutputPort;
import br.com.gabrieudev.recipes.domain.Recipe;

public class RecipeService implements RecipeinputPort {
    private final RecipeOutputPort recipeOutputPort;

    public RecipeService(RecipeOutputPort recipeOutputPort) {
        this.recipeOutputPort = recipeOutputPort;
    }

    @Override
    public Recipe create(Recipe recipe) {
        if (recipeOutputPort.existsByTitle(recipe.getTitle())) {
            throw new AlreadyExistsException("Já existe uma receita com esse título.");
        }

        return recipeOutputPort.create(recipe)
            .orElseThrow(() -> new InternalErrorException("Erro ao criar receita."));
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
    public List<Recipe> findAll(UUID userId, String title, Integer cookTimeMinutes, Integer servings, UUID categoryId) {
        return recipeOutputPort.findAll(userId, title, cookTimeMinutes, servings, categoryId);
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
