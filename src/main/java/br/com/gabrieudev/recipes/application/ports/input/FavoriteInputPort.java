package br.com.gabrieudev.recipes.application.ports.input;

import java.util.List;
import java.util.UUID;

import br.com.gabrieudev.recipes.domain.Favorite;

public interface FavoriteInputPort {
    Favorite create(Favorite favorite);
    Favorite update(Favorite favorite);
    void delete(UUID id);
    Favorite findById(UUID id);
    List<Favorite> findAll(UUID userId, Integer page, Integer size);
}
