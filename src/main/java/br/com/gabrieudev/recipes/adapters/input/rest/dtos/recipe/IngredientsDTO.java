package br.com.gabrieudev.recipes.adapters.input.rest.dtos.recipe;

import br.com.gabrieudev.recipes.adapters.input.rest.dtos.ingredient.IngredientDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IngredientsDTO {
    private IngredientDTO ingredient;
    private String quantity;
}
