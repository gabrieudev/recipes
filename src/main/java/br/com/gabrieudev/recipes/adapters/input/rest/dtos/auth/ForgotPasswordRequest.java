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
public class ForgotPasswordRequest {
    @Schema(
        description = "E-mail do usuário para recuperação de senha",
        example = "joao@gmail.com"
    )
    @NotBlank(message = "E-mail obrigatório")
    @Email(message = "E-mail inválido")
    private String email;
}
