package br.com.gabrieudev.recipes.adapters.input.rest.dtos.recipeingredient;

import java.util.UUID;

import org.modelmapper.ModelMapper;

import br.com.gabrieudev.recipes.adapters.input.rest.dtos.ingredient.IngredientDTO;
import br.com.gabrieudev.recipes.adapters.input.rest.dtos.recipe.RecipeDTO;
import br.com.gabrieudev.recipes.domain.RecipeIngredient;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRecipeIngredientDTO {
    @Schema(
        description = "Identificador do ingrediente da receita",
        example = "d1d1d1d1-d1d1-d1d1-d1d1-d1d1d1d1d1d1",
        required = true
    )
    private UUID id;

    private RecipeDTO recipe;
    
    private IngredientDTO ingredient;
    
    @Schema(
        example = "1kg",
        description = "Quantidade",
        required = true
    )
    private String quantity;

    public RecipeIngredient toDomainObj() {
        return new ModelMapper().map(this, RecipeIngredient.class);
    }
}
