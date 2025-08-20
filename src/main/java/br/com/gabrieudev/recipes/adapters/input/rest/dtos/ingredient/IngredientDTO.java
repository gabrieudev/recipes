package br.com.gabrieudev.recipes.adapters.input.rest.dtos.ingredient;

import java.util.UUID;

import org.modelmapper.ModelMapper;

import br.com.gabrieudev.recipes.domain.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IngredientDTO {
    private UUID id;
    private String name;
    private String description;

    public static IngredientDTO from(Ingredient ingredient) {
        return new ModelMapper().map(ingredient, IngredientDTO.class);
    }

    public Ingredient toDomainObj() {
        return new ModelMapper().map(this, Ingredient.class);
    }
}
