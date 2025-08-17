package br.com.gabrieudev.recipes.application.ports.output;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.gabrieudev.recipes.domain.Category;

public interface CategoryOutputPort {
    Optional<Category> create(Category category);
    Optional<Category> update(Category category);
    boolean delete(UUID id);
    Optional<Category> findById(UUID id);
    List<Category> findAll(String name, Integer page, Integer size);
    boolean existsById(UUID id);
    boolean existsByName(String name);
}
