package br.com.gabrieudev.recipes.adapters.input.rest.dtos.recipe;

import java.util.List;

import org.modelmapper.ModelMapper;

import br.com.gabrieudev.recipes.adapters.input.rest.dtos.category.CategoryDTO;
import br.com.gabrieudev.recipes.adapters.input.rest.dtos.recipeingredient.CreateRecipeIngredientDTO;
import br.com.gabrieudev.recipes.domain.Recipe;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateRecipeDTO {
    @Schema(
        example = "Lasanha",
        description = "Titulo da receita",
        required = true
    )
    private String title;

    @Schema(
        example = "Receita de lasanha",
        description = "Instruções para preparo da receita",
        required = true
    )
    private String instructions;

    @Schema(
        example = "30",
        description = "Tempo de preparo em minutos",
        required = true
    )
    private Integer cookTimeMinutes;
    
    @Schema(
        example = "4",
        description = "Quantidade de porções",
        required = true
    )
    private Integer servings;

    @Schema(
        example = "https://example.com/recipe.jpg",
        description = "URL da imagem da receita"
    )
    private String imageUrl;

    private CategoryDTO category;

    private List<CreateRecipeIngredientDTO> ingredients;
    
    public Recipe toDomainObj() {
        return new ModelMapper().map(this, Recipe.class);
    }
}
