package br.com.gabrieudev.recipes.adapters.input.rest.dtos.category;

import java.util.UUID;

import org.modelmapper.ModelMapper;

import br.com.gabrieudev.recipes.domain.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCategoryDTO {

    @Schema(
        description = "Identificador da categoria",
        example = "123e4567-e89b-12d3-a456-426614174000",
        required = true
    )
    private UUID id;

    @Schema(
        description = "Nome da categoria",
        example = "Massas",
        required = true
    )
    @NotBlank(message = "Nome obrigatório")
    private String name;

    @Schema(
        description = "Descrição da categoria",
        example = "Categorias de massas",
        required = false
    )
    private String description;

    public Category toDomainObj() {
        return new ModelMapper().map(this, Category.class);
    }
}
