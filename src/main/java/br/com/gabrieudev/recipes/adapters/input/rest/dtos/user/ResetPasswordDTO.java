package br.com.gabrieudev.recipes.adapters.input.rest.dtos.user;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordDTO {
    @Schema(
        description = "Código de redefinição de senha",
        example = "5c5b2b6f-7d0c-4c7a-8b4b-5c5b2b6f7d0c"
    )
    @NotNull(message = "Código obrigatório")
    private UUID code;

    @Schema(
        description = "Nova senha do usuário",
        example = "As25#5!D"
    )
    @NotBlank(message = "Nova senha obrigatória")
    private String password;
}
