package br.com.gabrieudev.recipes.adapters.input.rest.dtos.userrole;

import org.modelmapper.ModelMapper;

import br.com.gabrieudev.recipes.adapters.input.rest.dtos.role.RoleDTO;
import br.com.gabrieudev.recipes.adapters.input.rest.dtos.user.UserDTO;
import br.com.gabrieudev.recipes.domain.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRoleDTO {
    @Schema(
        description = "Identificador do usuário",
        example = "123e4567-e89b-12d3-a456-426614174000",
        required = true
    )
    @NotNull(message = "Usuário obrigatório")
    private UserDTO user;

    @Schema(
        description = "Identificador da Role",
        example = "123e4567-e89b-12d3-a456-426614174000",
        required = true
    )
    @NotNull(message = "Role obrigatória")
    private RoleDTO role;

    public UserRole toDomainObj() {
        return new ModelMapper().map(this, UserRole.class);
    }
}
