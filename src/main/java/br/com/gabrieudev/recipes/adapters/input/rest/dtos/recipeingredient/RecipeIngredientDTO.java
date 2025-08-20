package br.com.gabrieudev.recipes.adapters.input.rest.dtos.recipeingredient;

import java.util.UUID;

import org.modelmapper.ModelMapper;

import br.com.gabrieudev.recipes.adapters.input.rest.dtos.ingredient.IngredientDTO;
import br.com.gabrieudev.recipes.adapters.input.rest.dtos.recipe.RecipeDTO;
import br.com.gabrieudev.recipes.domain.RecipeIngredient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeIngredientDTO {
    private UUID id;
    private RecipeDTO recipe;
    private IngredientDTO ingredient;
    private String quantity;

    public static RecipeIngredientDTO from(RecipeIngredient recipeIngredient) {
        return new ModelMapper().map(recipeIngredient, RecipeIngredientDTO.class);
    }

    public RecipeIngredient toDomainObj() {
        return new ModelMapper().map(this, RecipeIngredient.class);
    }
}
