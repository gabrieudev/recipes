package br.com.gabrieudev.recipes.adapters.output.persistence.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import br.com.gabrieudev.recipes.adapters.output.persistence.entities.JpaFavoriteEntity;
import br.com.gabrieudev.recipes.adapters.output.persistence.entities.JpaRecipeEntity;
import br.com.gabrieudev.recipes.adapters.output.persistence.entities.JpaUserEntity;
import br.com.gabrieudev.recipes.adapters.output.persistence.repositories.jpa.JpaFavoriteRepository;
import br.com.gabrieudev.recipes.adapters.output.persistence.repositories.jpa.JpaRecipeRepository;
import br.com.gabrieudev.recipes.adapters.output.persistence.repositories.jpa.JpaUserRepository;
import br.com.gabrieudev.recipes.application.ports.output.FavoriteOutputPort;
import br.com.gabrieudev.recipes.domain.Favorite;

@Component
public class FavoriteRepositoryAdapter implements FavoriteOutputPort {
    private final JpaFavoriteRepository jpaFavoriteRepository;
    private final JpaUserRepository jpaUserRepository;
    private final JpaRecipeRepository jpaRecipeRepository;

    public FavoriteRepositoryAdapter(JpaFavoriteRepository jpaFavoriteRepository, JpaUserRepository jpaUserRepository,
            JpaRecipeRepository jpaRecipeRepository) {
        this.jpaFavoriteRepository = jpaFavoriteRepository;
        this.jpaUserRepository = jpaUserRepository;
        this.jpaRecipeRepository = jpaRecipeRepository;
    }

    @Override
    public Optional<Favorite> create(Favorite favorite) {
        try {
            JpaFavoriteEntity savedFavorite = jpaFavoriteRepository.save(JpaFavoriteEntity.fromDomainObj(favorite));

            return Optional.of(savedFavorite.toDomainObj());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean delete(UUID id) {
        try {
            jpaFavoriteRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaFavoriteRepository.existsById(id);
    }

    @Override
    public boolean existsByUserIdAndRecipeId(UUID userId, UUID recipeId) {
        try {
            JpaUserEntity user = jpaUserRepository.findById(userId).get();
            JpaRecipeEntity recipe = jpaRecipeRepository.findById(recipeId).get();

            return jpaFavoriteRepository.existsByUserAndRecipe(user, recipe);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<Favorite> findAll(UUID userId, Integer page, Integer size) {
        return jpaFavoriteRepository.findAll(userId, PageRequest.of(page, size))
                .stream()
                .map(JpaFavoriteEntity::toDomainObj)
                .toList();
    }

    @Override
    public Optional<Favorite> findById(UUID id) {
        return jpaFavoriteRepository.findById(id)
                .map(JpaFavoriteEntity::toDomainObj);
    }
}
