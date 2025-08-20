package br.com.gabrieudev.recipes.adapters.input.rest.dtos.favorite;

import java.time.LocalDateTime;
import java.util.UUID;

import org.modelmapper.ModelMapper;

import br.com.gabrieudev.recipes.adapters.input.rest.dtos.recipe.RecipeDTO;
import br.com.gabrieudev.recipes.adapters.input.rest.dtos.user.UserDTO;
import br.com.gabrieudev.recipes.domain.Favorite;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteDTO {
    private UUID id;
    private UserDTO user;
    private RecipeDTO recipe;
    private LocalDateTime favoritedAt;

    public static FavoriteDTO from(Favorite favorite) {
        return new ModelMapper().map(favorite, FavoriteDTO.class);
    }

    public static Favorite toDomainObj(FavoriteDTO favoriteDTO) {
        return new ModelMapper().map(favoriteDTO, Favorite.class);
    }
}
