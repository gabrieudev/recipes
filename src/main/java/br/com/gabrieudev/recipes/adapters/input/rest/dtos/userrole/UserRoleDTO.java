package br.com.gabrieudev.recipes.adapters.input.rest.dtos.userrole;

import java.util.UUID;

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
public class UserRoleDTO {
    private UUID id;

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

    public static UserRoleDTO from(UserRole userRole) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(userRole, UserRoleDTO.class);
    }

    public UserRole toDomainObj() {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, UserRole.class);
    }
}
