package br.com.gabrieudev.recipes.application.ports.output;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.gabrieudev.recipes.domain.Ingredient;

public interface IngredientOutputPort {
    Optional<Ingredient> create(Ingredient ingredient);
    Optional<Ingredient> update(Ingredient ingredient);
    boolean delete(UUID id);
    Optional<Ingredient> findById(UUID id);
    List<Ingredient> findAll(String name, Integer page, Integer size);
    boolean existsById(UUID id);
    boolean existsByName(String name);
}
