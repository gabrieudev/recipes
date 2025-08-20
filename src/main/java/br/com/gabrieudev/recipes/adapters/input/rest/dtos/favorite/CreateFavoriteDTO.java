package br.com.gabrieudev.recipes.adapters.input.rest.dtos.favorite;

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
public class CreateFavoriteDTO {
    private UserDTO user;
    private RecipeDTO recipe;

    public Favorite toDomainObj() {
        return new ModelMapper().map(this, Favorite.class);
    }
}
