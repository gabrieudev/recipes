package br.com.gabrieudev.recipes.application.ports.input;

import java.util.List;
import java.util.UUID;

import br.com.gabrieudev.recipes.domain.Category;

public interface CategoryInputPort {
    Category create(Category category);
    Category update(Category category);
    void delete(UUID id);
    Category findById(UUID id);
    List<Category> findAll(String name, Integer page, Integer size);
}
