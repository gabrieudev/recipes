package br.com.gabrieudev.recipes.application.services;

import java.util.List;
import java.util.UUID;

import br.com.gabrieudev.recipes.application.exceptions.InternalErrorException;
import br.com.gabrieudev.recipes.application.exceptions.NotFoundException;
import br.com.gabrieudev.recipes.application.ports.input.CategoryInputPort;
import br.com.gabrieudev.recipes.application.ports.output.CategoryOutputPort;
import br.com.gabrieudev.recipes.domain.Category;

public class CategoryService implements CategoryInputPort {
    private final CategoryOutputPort categoryOutputPort;

    public CategoryService(CategoryOutputPort categoryOutputPort) {
        this.categoryOutputPort = categoryOutputPort;
    }

    @Override
    public Category create(Category category) {
        if (categoryOutputPort.existsByName(category.getName())) {
            throw new NotFoundException("Já existe uma categoria com esse nome.");
        }

        return categoryOutputPort.create(category)
            .orElseThrow(() -> new InternalErrorException("Erro ao criar categoria."));
    }

    @Override
    public void delete(UUID id) {
        if (!categoryOutputPort.existsById(id)) {
            throw new NotFoundException("Categoria não encontrada.");
        }

        if (!categoryOutputPort.delete(id)) {
            throw new InternalErrorException("Erro ao deletar categoria.");
        }
    }

    @Override
    public List<Category> findAll(String name, Integer page, Integer size) {
        return categoryOutputPort.findAll(name, page, size);
    }

    @Override
    public Category findById(UUID id) {
        return categoryOutputPort.findById(id)
            .orElseThrow(() -> new NotFoundException("Categoria não encontrada."));
    }

    @Override
    public Category update(Category category) {
        Category categoryToUpdate = categoryOutputPort.findById(category.getId())
            .orElseThrow(() -> new NotFoundException("Categoria não encontrada."));

        if (!categoryToUpdate.getName().equals(category.getName()) && categoryOutputPort.existsByName(category.getName())) {
            throw new NotFoundException("Já existe uma categoria com esse nome.");
        }

        return categoryOutputPort.update(category)
            .orElseThrow(() -> new InternalErrorException("Erro ao atualizar categoria."));
    }
    
}
