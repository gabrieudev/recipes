package br.com.gabrieudev.recipes.adapters.input.rest.dtos.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenRequest {
    @Schema(
        description = "Refresh token do usuário"
    )
    @NotBlank(message = "Refresh token obrigatório")
    private String refreshToken;
}
