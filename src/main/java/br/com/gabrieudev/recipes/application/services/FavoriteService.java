package br.com.gabrieudev.recipes.application.services;

import java.util.List;
import java.util.UUID;

import br.com.gabrieudev.recipes.application.exceptions.InternalErrorException;
import br.com.gabrieudev.recipes.application.exceptions.NotFoundException;
import br.com.gabrieudev.recipes.application.ports.input.FavoriteInputPort;
import br.com.gabrieudev.recipes.application.ports.output.FavoriteOutputPort;
import br.com.gabrieudev.recipes.domain.Favorite;

public class FavoriteService implements FavoriteInputPort {
    private final FavoriteOutputPort favoriteOutputPort;

    public FavoriteService(FavoriteOutputPort favoriteOutputPort) {
        this.favoriteOutputPort = favoriteOutputPort;
    }

    @Override
    public Favorite create(Favorite favorite) {
        if (favoriteOutputPort.existsByUserIdAndRecipeId(favorite.getUser().getId(), favorite.getRecipe().getId())) {
            throw new InternalErrorException("Receita já favoritada.");
        }

        return favoriteOutputPort.create(favorite)
            .orElseThrow(() -> new InternalErrorException("Erro ao favoritar receita."));
    }

    @Override
    public void delete(UUID id) {
        if (!favoriteOutputPort.existsById(id)) {
            throw new NotFoundException("Receita não encontrada.");
        }

        if (!favoriteOutputPort.delete(id)) {
            throw new InternalErrorException("Erro ao desfavoritar receita.");
        }
    }

    @Override
    public List<Favorite> findAll(UUID userId, Integer page, Integer size) {
        return favoriteOutputPort.findAll(userId, page, size);
    }

    @Override
    public Favorite findById(UUID id) {
        return favoriteOutputPort.findById(id)
            .orElseThrow(() -> new NotFoundException("Receita não encontrada."));
    }
}
