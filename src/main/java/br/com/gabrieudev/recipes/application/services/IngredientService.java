package br.com.gabrieudev.recipes.application.services;

import java.util.List;
import java.util.UUID;

import br.com.gabrieudev.recipes.application.exceptions.AlreadyExistsException;
import br.com.gabrieudev.recipes.application.exceptions.InternalErrorException;
import br.com.gabrieudev.recipes.application.exceptions.NotFoundException;
import br.com.gabrieudev.recipes.application.ports.input.IngredientInputPort;
import br.com.gabrieudev.recipes.application.ports.output.IngredientOutputPort;
import br.com.gabrieudev.recipes.domain.Ingredient;

public class IngredientService implements IngredientInputPort {
    private final IngredientOutputPort ingredientOutputPort;

    public IngredientService(IngredientOutputPort ingredientOutputPort) {
        this.ingredientOutputPort = ingredientOutputPort;
    }

    @Override
    public Ingredient create(Ingredient ingredient) {
        if (ingredientOutputPort.existsByName(ingredient.getName())) {
            throw new AlreadyExistsException("Já existe um ingrediente com esse nome.");
        }

        return ingredientOutputPort.create(ingredient)
            .orElseThrow(() -> new InternalErrorException("Erro ao criar ingrediente."));
    }

    @Override
    public void delete(UUID id) {
        if (!ingredientOutputPort.existsById(id)) {
            throw new NotFoundException("Ingrediente não encontrado.");
        }

        if (!ingredientOutputPort.delete(id)) {
            throw new InternalErrorException("Erro ao deletar ingrediente.");
        }
    }

    @Override
    public List<Ingredient> findAll(String name, Integer page, Integer size) {
        return ingredientOutputPort.findAll(name, page, size);
    }

    @Override
    public Ingredient findById(UUID id) {
        return ingredientOutputPort.findById(id)
            .orElseThrow(() -> new NotFoundException("Ingrediente não encontrado."));
    }

    @Override
    public Ingredient update(Ingredient ingredient) {
        Ingredient ingredientToUpdate = ingredientOutputPort.findById(ingredient.getId())
            .orElseThrow(() -> new NotFoundException("Ingrediente não encontrado."));

        if (!ingredientToUpdate.getName().equals(ingredient.getName()) && ingredientOutputPort.existsByName(ingredient.getName())) {
            throw new NotFoundException("Já existe um ingrediente com esse nome.");
        }

        return ingredientOutputPort.update(ingredient)
            .orElseThrow(() -> new InternalErrorException("Erro ao atualizar ingrediente."));
    }
}
