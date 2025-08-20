package br.com.gabrieudev.recipes.adapters.input.rest.dtos.category;

import java.util.UUID;

import org.modelmapper.ModelMapper;

import br.com.gabrieudev.recipes.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    private UUID id;
    private String name;
    private String description;

    public static CategoryDTO from(Category category) {
        return new ModelMapper().map(category, CategoryDTO.class);
    }

    public Category toDomainObj() {
        return new ModelMapper().map(this, Category.class);
    }
}
