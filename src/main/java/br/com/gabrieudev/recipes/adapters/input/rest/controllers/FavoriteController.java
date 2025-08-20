package br.com.gabrieudev.recipes.adapters.input.rest.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.gabrieudev.recipes.adapters.input.rest.dtos.ApiResponseDTO;
import br.com.gabrieudev.recipes.adapters.input.rest.dtos.favorite.CreateFavoriteDTO;
import br.com.gabrieudev.recipes.adapters.input.rest.dtos.favorite.FavoriteDTO;
import br.com.gabrieudev.recipes.application.ports.input.FavoriteInputPort;
import br.com.gabrieudev.recipes.domain.Favorite;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/favorites")
public class FavoriteController {
    private final FavoriteInputPort favoriteInputPort;

    public FavoriteController(FavoriteInputPort favoriteInputPort) {
        this.favoriteInputPort = favoriteInputPort;
    }

    @Operation(
        summary = "Favoritar uma receita",
        description = "Endpoint para favoritar uma receita.",
        tags = { "Favorite" },
        security = @SecurityRequirement(name = "Auth")
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "201",
                description = "Receita favoritada com sucesso."
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Token inválido.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                        implementation = Void.class
                    )
                )
            ),
            @ApiResponse(
                responseCode = "422",
                description = "Dados inválidos.",
                content = @Content(
                    schema = @Schema(implementation = ApiResponseDTO.class)
                )
            ),
            @ApiResponse(
                responseCode = "409",
                description = "Receita já favoritada.",
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
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    @PostMapping
    public ResponseEntity<ApiResponseDTO<FavoriteDTO>> create(
        @Valid
        @RequestBody
        CreateFavoriteDTO createFavoriteDTO,

        HttpServletRequest request
    ) {
        log.info("POST /api/v1/favorites | Client: {}", request.getRemoteAddr());

        Favorite favorite = favoriteInputPort.create(createFavoriteDTO.toDomainObj());

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDTO.created(FavoriteDTO.from(favorite)));
    }

    @Operation(
        summary = "Listar favoritos",
        description = "Endpoint para listagem de favoritos.",
        tags = { "Favorite" },
        security = @SecurityRequirement(name = "Auth")
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Favoritos listados com sucesso."
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Token inválido.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                        implementation = Void.class
                    )
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
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<FavoriteDTO>>> findAll(
        @Schema(
            name = "userId",
            description = "Id do usuário",
            example = "c5f0f6b3-8a9c-4a1b-8e9c-4a1b8e9c4a1b"
        )
        @RequestParam(required = false) UUID userId,

        @Schema(
            name = "page",
            description = "Página",
            example = "1"
        )
        @RequestParam(required = true) Integer page,
        
        @Schema(
            name = "size",
            description = "Quantidade de registros por página",
            example = "10"
        )
        @RequestParam(required = true) Integer size,

        HttpServletRequest request
    ) {
        log.info("GET /api/v1/favorites | Client: {}", request.getRemoteAddr());

        List<FavoriteDTO> favorites = favoriteInputPort.findAll(userId, page, size)
            .stream()
            .map(FavoriteDTO::from)
            .toList();

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDTO.ok(favorites));
    }

    @Operation(
        summary = "Obter um favorito",
        description = "Endpoint para obter um favorito.",
        tags = { "Favorite" },
        security = @SecurityRequirement(name = "Auth")
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Favorito obtido com sucesso."
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Token inválido.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                        implementation = Void.class
                    )   
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Favorito não encontrado.",
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
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<FavoriteDTO>> findById(
        @Schema(
            description = "ID do favorito",
            example = "123e4567-e89b-12d3-a456-426614174000"
        )
        @PathVariable UUID id,

        HttpServletRequest request
    ) {
        log.info("GET /api/v1/favorites/{id} | Client: {}", request.getRemoteAddr());

        Favorite favorite = favoriteInputPort.findById(id);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDTO.ok(FavoriteDTO.from(favorite)));
    }

    @Operation(
        summary = "Excluir um favorito",
        description = "Endpoint para exclusão de um favorito.",
        tags = { "Favorite" },
        security = @SecurityRequirement(name = "Auth")
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "204",
                description = "Favorito excluido com sucesso."
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Token inválido.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                        implementation = Void.class
                    )
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Favorito não encontrado.",
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
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<String>> delete(
        @Schema(
            description = "ID do favorito",
            example = "123e4567-e89b-12d3-a456-426614174000"
        )
        @PathVariable UUID id,

        HttpServletRequest request
    ) {
        log.info("DELETE /api/v1/favorites/{id} | Client: {}", request.getRemoteAddr());

        favoriteInputPort.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponseDTO.noContent("Favorito excluido com sucesso."));
    }
}
