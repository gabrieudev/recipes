package br.com.gabrieudev.recipes.adapters.input.rest.dtos.user;

import org.modelmapper.ModelMapper;

import br.com.gabrieudev.recipes.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserDTO {
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

    @Schema(
        description = "E-mail do usuário",
        example = "joão@gmail.com",
        required = true
    )
    @NotBlank(message = "E-mail obrigatório")
    @Email(message = "E-mail inválido")
    private String email;

    @Schema(
        description = "Senha do usuário",
        example = "As25#5!D",
        required = true
    )
    @NotBlank(message = "Senha obrigatória")
    private String password;

    public User toDomainObj() {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, User.class);
    }
}
