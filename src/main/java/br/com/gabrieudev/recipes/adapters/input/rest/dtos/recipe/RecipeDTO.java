package br.com.gabrieudev.recipes.adapters.input.rest.dtos.recipe;

import java.time.LocalDateTime;
import java.util.UUID;

import org.modelmapper.ModelMapper;

import br.com.gabrieudev.recipes.adapters.input.rest.dtos.category.CategoryDTO;
import br.com.gabrieudev.recipes.domain.Recipe;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeDTO {
    private UUID id;
    private String title;
    private String instructions;
    private Integer cookTimeMinutes;
    private Integer servings;
    private String imageUrl;
    private CategoryDTO category;
    private LocalDateTime createdAt;

    public static RecipeDTO from(Recipe recipe) {
        return new ModelMapper().map(recipe, RecipeDTO.class);
    }

    public static Recipe toDomainObj(RecipeDTO recipeDTO) {
        return new ModelMapper().map(recipeDTO, Recipe.class);
    }
}
