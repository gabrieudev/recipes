package br.com.gabrieudev.recipes.application.ports.input;

import java.util.List;
import java.util.UUID;

import br.com.gabrieudev.recipes.domain.Ingredient;

public interface IngredientInputPort {
    Ingredient create(Ingredient ingredient);
    Ingredient update(Ingredient ingredient);
    void delete(UUID id);
    Ingredient findById(UUID id);
    List<Ingredient> findAll(String name, Integer page, Integer size);
}
