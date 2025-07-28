package br.com.gabrieudev.recipes.adapters.input.rest.dtos.user;

import java.time.LocalDateTime;
import java.util.UUID;

import org.modelmapper.ModelMapper;

import br.com.gabrieudev.recipes.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    @Schema(
        description = "Identificador do usuário",
        example = "123e4567-e89b-12d3-a456-426614174000"
    )
    private UUID id;
    
    @Schema(
        description = "Nome do usuário",
        example = "João"
    )
    private String firstName;
    
    @Schema(
        description = "Sobrenome do usuário",
        example = "Silva"
    )
    private String lastName;
    
    @Schema(
        description = "E-mail do usuário",
        example = "joão@gmail.com"
    )
    private String email;
    
    @Schema(
        description = "Indica se o usuário está ativo",
        example = "true"
    )
    private Boolean isActive;
    
    @Schema(
        description = "Data e hora da criação do usuário",
        example = "2023-01-01T00:00:00"
    )
    private LocalDateTime createdAt;
    
    @Schema(
        description = "Data e hora da atualização do usuário",
        example = "2023-01-01T00:00:00"
    )
    private LocalDateTime updatedAt;

    public static UserDTO from(User user) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(user, UserDTO.class);
    }

    public User toDomainObj() {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, User.class);
    }
}
