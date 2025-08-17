package br.com.gabrieudev.recipes.adapters.output.persistence.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import br.com.gabrieudev.recipes.adapters.output.persistence.entities.JpaIngredientEntity;
import br.com.gabrieudev.recipes.adapters.output.persistence.repositories.jpa.JpaIngredientRepository;
import br.com.gabrieudev.recipes.application.exceptions.NotFoundException;
import br.com.gabrieudev.recipes.application.ports.output.IngredientOutputPort;
import br.com.gabrieudev.recipes.domain.Ingredient;

@Component
public class IngredientRepositoryAdapter implements IngredientOutputPort {
    private final JpaIngredientRepository jpaIngredientRepository;

    public IngredientRepositoryAdapter(JpaIngredientRepository jpaIngredientRepository) {
        this.jpaIngredientRepository = jpaIngredientRepository;
    }

    @Override
    public Optional<Ingredient> create(Ingredient ingredient) {
        try {
            JpaIngredientEntity savedIngredient = jpaIngredientRepository.save(JpaIngredientEntity.fromDomainObj(ingredient));

            return Optional.of(savedIngredient.toDomainObj());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean delete(UUID id) {
        try {
            jpaIngredientRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaIngredientRepository.existsById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return jpaIngredientRepository.existsByName(name);
    }

    @Override
    public List<Ingredient> findAll(String name, Integer page, Integer size) {
        return jpaIngredientRepository.findAll(name, PageRequest.of(page, size))
            .stream()
            .map(JpaIngredientEntity::toDomainObj)
            .toList();
    }

    @Override
    public Optional<Ingredient> findById(UUID id) {
        return jpaIngredientRepository.findById(id)
            .map(JpaIngredientEntity::toDomainObj);
    }

    @Override
    public Optional<Ingredient> update(Ingredient ingredient) {
        try {
            JpaIngredientEntity ingredientToUpdate = jpaIngredientRepository.findById(ingredient.getId())
                .orElseThrow(() -> new NotFoundException("Ingrediente não encontrado."));

            ingredientToUpdate.update(ingredient);

            return Optional.of(jpaIngredientRepository.save(ingredientToUpdate).toDomainObj());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
