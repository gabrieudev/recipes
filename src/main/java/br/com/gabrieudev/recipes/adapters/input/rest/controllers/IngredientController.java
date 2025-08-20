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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.gabrieudev.recipes.adapters.input.rest.dtos.ApiResponseDTO;
import br.com.gabrieudev.recipes.adapters.input.rest.dtos.ingredient.CreateIngredientDTO;
import br.com.gabrieudev.recipes.adapters.input.rest.dtos.ingredient.IngredientDTO;
import br.com.gabrieudev.recipes.adapters.input.rest.dtos.ingredient.UpdateIngredientDTO;
import br.com.gabrieudev.recipes.application.ports.input.IngredientInputPort;
import br.com.gabrieudev.recipes.domain.Ingredient;
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
@RequestMapping("/ingredients")
public class IngredientController {
    private final IngredientInputPort ingredientInputPort;

    public IngredientController(IngredientInputPort ingredientInputPort) {
        this.ingredientInputPort = ingredientInputPort;
    }

    @Operation(
        summary = "Criar um ingrediente",
        description = "Endpoint para criação de um ingrediente.",
        tags = { "Ingredient" },
        security = @SecurityRequirement(name = "Auth")
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "201",
                description = "Ingrediente criado com sucesso."
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
                description = "Ingrediente já criado.",
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
    @PostMapping
    public ResponseEntity<ApiResponseDTO<IngredientDTO>> create(
        @Valid
        @RequestBody
        CreateIngredientDTO createIngredientDTO,

        HttpServletRequest request
    ) {
        log.info("POST /api/v1/ingredients | Client: {}", request.getRemoteAddr());

        Ingredient ingredient = ingredientInputPort.create(createIngredientDTO.toDomainObj());

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDTO.created(IngredientDTO.from(ingredient)));
    }

    @Operation(
        summary = "Atualizar um ingrediente",
        description = "Endpoint para atualização de um ingrediente.",
        tags = { "Ingredient" },
        security = @SecurityRequirement(name = "Auth")
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Ingrediente atualizado com sucesso."
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
                description = "Ingrediente não encontrado.",
                content = @Content(
                    schema = @Schema(implementation = ApiResponseDTO.class)
                )
            ),
            @ApiResponse(
                responseCode = "409",
                description = "Ingrediente já cadastrado.",
                content = @Content(
                    schema = @Schema(implementation = ApiResponseDTO.class)
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
                responseCode = "500",
                description = "Erro interno do servidor.",
                content = @Content(
                    schema = @Schema(implementation = ApiResponseDTO.class)
                )
            )
        }
    )
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PutMapping
    public ResponseEntity<ApiResponseDTO<IngredientDTO>> update(
        @Valid
        @RequestBody
        UpdateIngredientDTO updateIngredientDTO,

        HttpServletRequest request
    ) {
        log.info("PUT /api/v1/ingredients | Client: {}", request.getRemoteAddr());

        Ingredient ingredient = ingredientInputPort.update(updateIngredientDTO.toDomainObj());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDTO.ok(IngredientDTO.from(ingredient)));
    }

    @Operation(
        summary = "Listar ingredientes",
        description = "Endpoint para listagem de ingredientes.",
        tags = { "Ingredient" },
        security = @SecurityRequirement(name = "Auth")
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Ingredientes listados com sucesso."
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
    public ResponseEntity<ApiResponseDTO<List<IngredientDTO>>> findAll(
        @Schema(
            name = "name",
            description = "Nome do ingrediente",
            example = "Sal"
        )
        @RequestParam(required = false) String name,

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
        log.info("GET /api/v1/ingredients | Client: {}", request.getRemoteAddr());

        List<IngredientDTO> ingredients = ingredientInputPort.findAll(name, page, size)
            .stream()
            .map(IngredientDTO::from)
            .toList();

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDTO.ok(ingredients));
    }

    @Operation(
        summary = "Obter um ingrediente",
        description = "Endpoint para obter um ingrediente.",
        tags = { "Ingredient" },
        security = @SecurityRequirement(name = "Auth")
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Ingrediente obtido com sucesso."
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
                description = "Ingrediente não encontrado.",
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
    public ResponseEntity<ApiResponseDTO<IngredientDTO>> findById(
        @Schema(
            description = "ID do ingrediente",
            example = "123e4567-e89b-12d3-a456-426614174000"
        )
        @PathVariable UUID id,

        HttpServletRequest request
    ) {
        log.info("GET /api/v1/ingredients/{id} | Client: {}", request.getRemoteAddr());

        Ingredient ingredient = ingredientInputPort.findById(id);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDTO.ok(IngredientDTO.from(ingredient)));
    }

    @Operation(
        summary = "Excluir um ingrediente",
        description = "Endpoint para exclusão de um ingrediente.",
        tags = { "Ingredient" },
        security = @SecurityRequirement(name = "Auth")
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "204",
                description = "Ingrediente excluido com sucesso."
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
                description = "Ingrediente não encontrado.",
                content = @Content(
                    schema = @Schema(implementation = ApiResponseDTO.class)
                )
            ),
            @ApiResponse(
                responseCode = "409",
                description = "Ingrediente possui receitas associadas.",
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
            description = "ID do ingrediente",
            example = "123e4567-e89b-12d3-a456-426614174000"
        )
        @PathVariable UUID id,

        HttpServletRequest request
    ) {
        log.info("DELETE /api/v1/ingredients/{id} | Client: {}", request.getRemoteAddr());

        ingredientInputPort.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponseDTO.noContent("Ingrediente excluido com sucesso."));
    }
}
