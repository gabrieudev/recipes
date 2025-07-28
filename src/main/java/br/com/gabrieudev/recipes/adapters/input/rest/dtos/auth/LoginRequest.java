package br.com.gabrieudev.recipes.adapters.input.rest.dtos.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
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
        example = "12345678",
        required = true
    )
    @NotBlank(message = "Senha obrigatória")
    private String password;
}
