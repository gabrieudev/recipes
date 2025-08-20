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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.gabrieudev.recipes.adapters.input.rest.dtos.ApiResponseDTO;
import br.com.gabrieudev.recipes.adapters.input.rest.dtos.recipeingredient.RecipeIngredientDTO;
import br.com.gabrieudev.recipes.application.ports.input.RecipeIngredientInputPort;
import br.com.gabrieudev.recipes.domain.RecipeIngredient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/recipes/{recipeId}/ingredients")
public class RecipeIngredientController {
    private final RecipeIngredientInputPort recipeIngredientInputPort;

    public RecipeIngredientController(RecipeIngredientInputPort recipeIngredientInputPort) {
        this.recipeIngredientInputPort = recipeIngredientInputPort;
    }

    @Operation(
        summary = "Listar ingredientes de receitas",
        description = "Endpoint para listagem de ingredientes de receitas.",
        tags = { "RecipeIngredient" },
        security = @SecurityRequirement(name = "Auth")
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Ingredientes de receitas listados com sucesso."
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
    public ResponseEntity<ApiResponseDTO<List<RecipeIngredientDTO>>> findAll(
        @Schema(
            name = "recipeId",
            description = "ID da receita",
            example = "c5f0f6b3-8a9c-4a1b-8e9c-4a1b8e9c4a1b"
        )
        @PathVariable UUID recipeId,

        @Schema(
            name = "ingredientId",
            description = "ID do ingrediente",
            example = "c5f0f6b3-8a9c-4a1b-8e9c-4a1b8e9c4a1b"
        )
        @RequestParam(required = false) UUID ingredientId,

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
        log.info("GET /api/v1/recipes/{recipeId}/ingredients | Client: {}", request.getRemoteAddr());

        List<RecipeIngredientDTO> recipeIngredients = recipeIngredientInputPort.findAll(recipeId, ingredientId, page, size)
            .stream()
            .map(RecipeIngredientDTO::from)
            .toList();

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDTO.ok(recipeIngredients));
    }

    @Operation(
        summary = "Obter um ingrediente de uma receita",
        description = "Endpoint para obter um ingrediente de uma receita.",
        tags = { "RecipeIngredient" },
        security = @SecurityRequirement(name = "Auth")
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Ingrediente de uma receita obtido com sucesso."
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
                description = "Ingrediente de uma receita não encontrado.",
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
    public ResponseEntity<ApiResponseDTO<RecipeIngredientDTO>> findById(
        @Schema(
            description = "ID do ingrediente de uma receita",
            example = "123e4567-e89b-12d3-a456-426614174000"
        )
        @PathVariable UUID id,

        HttpServletRequest request
    ) {
        log.info("GET /api/v1/recipes/{recipeId}/ingredients/{id} | Client: {}", request.getRemoteAddr());

        RecipeIngredient recipeIngredient = recipeIngredientInputPort.findById(id);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDTO.ok(RecipeIngredientDTO.from(recipeIngredient)));
    }

    @Operation(
        summary = "Excluir um ingrediente de uma receita",
        description = "Endpoint para exclusão de um ingrediente de uma receita.",
        tags = { "RecipeIngredient" },
        security = @SecurityRequirement(name = "Auth")
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "204",
                description = "Ingrediente de uma receita excluido com sucesso."
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
                description = "Ingrediente de uma receita não encontrado.",
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
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<String>> delete(
        @Schema(
            description = "ID do ingrediente de uma receita",
            example = "123e4567-e89b-12d3-a456-426614174000"
        )
        @PathVariable UUID id,

        HttpServletRequest request
    ) {
        log.info("DELETE /api/v1/recipes/{recipeId}/ingredients/{id} | Client: {}", request.getRemoteAddr());

        recipeIngredientInputPort.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponseDTO.noContent("Ingrediente de uma receita excluida com sucesso."));
    }
}
