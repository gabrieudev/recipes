package br.com.gabrieudev.recipes.application.ports.output;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.gabrieudev.recipes.domain.Favorite;

public interface FavoriteOutputPort {
    Optional<Favorite> create(Favorite favorite);
    Optional<Favorite> update(Favorite favorite);
    boolean delete(UUID id);
    Optional<Favorite> findById(UUID id);
    List<Favorite> findAll(UUID userId, Integer page, Integer size);
    boolean existsById(UUID id);
    boolean existsByUserIdAndRecipeId(UUID userId, UUID recipeId);
}
