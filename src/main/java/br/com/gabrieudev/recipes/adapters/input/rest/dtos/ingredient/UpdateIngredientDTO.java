package br.com.gabrieudev.recipes.adapters.input.rest.dtos.ingredient;

import java.util.UUID;

import org.modelmapper.ModelMapper;

import br.com.gabrieudev.recipes.domain.Ingredient;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateIngredientDTO {
    @Schema(
        description = "Identificador do ingrediente",
        example = "c5f0f6b3-8a9c-4a1b-8e9c-4a1b8e9c4a1b",
        required = true
    )
    private UUID id;

    @Schema(
        description = "Nome do ingrediente",
        example = "Sal",
        required = true
    )
    @NotBlank(message = "Nome obrigatório")
    private String name;

    @Schema(
        description = "Descrição do ingrediente",
        example = "Ingrediente de sal",
        required = false
    )
    private String description;

    public Ingredient toDomainObj() {
        return new ModelMapper().map(this, Ingredient.class);
    }
}
