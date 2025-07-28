package br.com.gabrieudev.recipes.adapters.input.rest.dtos.user;

import java.util.UUID;

import org.modelmapper.ModelMapper;

import br.com.gabrieudev.recipes.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDTO {
    @Schema(
        description = "Identificador do usuário",
        example = "123e4567-e89b-12d3-a456-426614174000",
        required = true
    )
    @NotNull(message = "Identificador do usuário obrigatório")
    private UUID id;

    @Schema(
        description = "Nome do usuário",
        example = "João",
        required = true
    )
    @NotBlank(message = "Nome obrigatório")
    private String firstName;

    @Schema(
        description = "Sobrenome do usuário",
        example = "Silva",
        required = true
    )
    @NotBlank(message = "Sobrenome obrigatório")
    private String lastName;

    public User toDomainObj() {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, User.class);
    }
}
