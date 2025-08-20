package br.com.gabrieudev.recipes.adapters.input.rest.dtos.category;

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
public class CreateCategoryDTO {
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
