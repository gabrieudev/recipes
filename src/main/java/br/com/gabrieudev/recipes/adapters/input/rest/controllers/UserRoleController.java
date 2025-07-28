package br.com.gabrieudev.recipes.adapters.input.rest.controllers;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.gabrieudev.recipes.adapters.input.rest.dtos.ApiResponseDTO;
import br.com.gabrieudev.recipes.adapters.input.rest.dtos.userrole.UserRoleDTO;
import br.com.gabrieudev.recipes.application.ports.input.UserRoleInputPort;
import br.com.gabrieudev.recipes.domain.UserRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@CrossOrigin
@RequestMapping("/users/{userId}/roles")
public class UserRoleController {
    private final UserRoleInputPort userRoleInputPort;

    public UserRoleController(UserRoleInputPort userRoleInputPort) {
        this.userRoleInputPort = userRoleInputPort;
    }

    @Operation(
        summary = "Associar um usuário à uma role",
        description = "Endpoint para associar um usuário à uma role.",
        tags = { "Role" },
        security = @SecurityRequirement(name = "Auth")
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "201",
                description = "Usuário associado com sucesso."
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Token inválido.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                        implementation = Void.class
                    )
                )
            ),
            @ApiResponse(
                responseCode = "409",
                description = "Usuário já associado à role.",
                content = @Content(
                    schema = @Schema(implementation = ApiResponseDTO.class)
                )
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Erro interno do servidor.",
                content = @Content(
                    schema = @Schema(implementation = ApiResponseDTO.class)
                )
            )
        }
    )
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PostMapping("/{roleId}")
    public ResponseEntity<ApiResponseDTO<UserRoleDTO>> assign(
        @Schema(
            description = "ID do usuário",
            example = "123e4567-e89b-12d3-a456-426614174000"
        )
        @PathVariable UUID userId,
        
        @Schema(
            description = "ID da role",
            example = "123e4567-e89b-12d3-a456-426614174000"
        )
        @PathVariable UUID roleId
    ) {
        UserRole userRole = userRoleInputPort.assign(userId, roleId);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDTO.ok(UserRoleDTO.from(userRole)));
    }

    @Operation(
        summary = "Desassociar um usuário de uma role",
        description = "Endpoint para desassociar um usuário de uma role.",
        tags = { "Role" },
        security = @SecurityRequirement(name = "Auth")
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "204",
                description = "Usuário desassociado com sucesso."
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Token inválido.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                        implementation = Void.class
                    )
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Usuário ou role não encontrados.",
                content = @Content(
                    schema = @Schema(implementation = ApiResponseDTO.class)
                )
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Erro interno do servidor.",
                content = @Content(
                    schema = @Schema(implementation = ApiResponseDTO.class)
                )
            )
        }
    )
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @DeleteMapping("/{roleId}")
    public ResponseEntity<ApiResponseDTO<String>> unassign(
        @Schema(
            description = "ID do usuário",
            example = "123e4567-e89b-12d3-a456-426614174000"
        )
        @PathVariable UUID userId,
        
        @Schema(
            description = "ID da role",
            example = "123e4567-e89b-12d3-a456-426614174000"
        )
        @PathVariable UUID roleId
    ) {
        userRoleInputPort.unassign(userId, roleId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponseDTO.noContent("Papel desassociado com sucesso."));
    }
}
