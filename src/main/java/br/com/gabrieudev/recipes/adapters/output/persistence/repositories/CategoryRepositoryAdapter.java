package br.com.gabrieudev.recipes.adapters.output.persistence.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import br.com.gabrieudev.recipes.adapters.output.persistence.entities.JpaCategoryEntity;
import br.com.gabrieudev.recipes.adapters.output.persistence.repositories.jpa.JpaCategoryRepository;
import br.com.gabrieudev.recipes.application.ports.output.CategoryOutputPort;
import br.com.gabrieudev.recipes.domain.Category;

@Component
public class CategoryRepositoryAdapter implements CategoryOutputPort {
    private JpaCategoryRepository jpaCategoryRepository;

    public CategoryRepositoryAdapter(JpaCategoryRepository jpaCategoryRepository) {
        this.jpaCategoryRepository = jpaCategoryRepository;
    }

    @Override
    public Optional<Category> create(Category category) {
        try {
            JpaCategoryEntity savedCategory = jpaCategoryRepository.save(JpaCategoryEntity.fromDomainObj(category));

            return Optional.of(savedCategory.toDomainObj());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean delete(UUID id) {
        try {
            jpaCategoryRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaCategoryRepository.existsById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return jpaCategoryRepository.existsByName(name);
    }

    @Override
    public List<Category> findAll(String name, Integer page, Integer size) {
        return jpaCategoryRepository.findAll(name, PageRequest.of(page, size))
            .stream()
            .map(JpaCategoryEntity::toDomainObj)
            .toList();
    }

    @Override
    public Optional<Category> findById(UUID id) {
        return jpaCategoryRepository.findById(id)
            .map(JpaCategoryEntity::toDomainObj);
    }

    @Override
    public Optional<Category> update(Category category) {
        try {
            JpaCategoryEntity categoryToUpdate = jpaCategoryRepository.findById(category.getId()).get();

            categoryToUpdate.update(category);

            return Optional.of(jpaCategoryRepository.save(categoryToUpdate).toDomainObj());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
