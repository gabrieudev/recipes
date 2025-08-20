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
import br.com.gabrieudev.recipes.adapters.input.rest.dtos.recipe.CreateRecipeDTO;
import br.com.gabrieudev.recipes.adapters.input.rest.dtos.recipe.RecipeDTO;
import br.com.gabrieudev.recipes.adapters.input.rest.dtos.recipe.UpdateRecipeDTO;
import br.com.gabrieudev.recipes.application.ports.input.RecipeinputPort;
import br.com.gabrieudev.recipes.domain.Recipe;
import br.com.gabrieudev.recipes.domain.RecipeIngredient;
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
@RequestMapping("/recipes")
public class RecipeController {
    private final RecipeinputPort recipeInputPort;

    public RecipeController(RecipeinputPort recipeInputPort) {
        this.recipeInputPort = recipeInputPort;
    }

    @Operation(
        summary = "Criar uma receita",
        description = "Endpoint para criação de uma receita.",
        tags = { "Recipe" },
        security = @SecurityRequirement(name = "Auth")
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "201",
                description = "Receita criado com sucesso."
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
                description = "Receita já criada.",
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
    public ResponseEntity<ApiResponseDTO<RecipeDTO>> create(
        @Valid 
        @RequestBody 
        CreateRecipeDTO createRecipeDTO,
        
        HttpServletRequest request
    ) {
        log.info("POST /api/v1/recipes | Client: {}", request.getRemoteAddr());

        Recipe recipe = createRecipeDTO.toDomainObj();

        List<RecipeIngredient> ingredients = createRecipeDTO.getIngredients().stream()
            .map(ingredientDTO -> {
                RecipeIngredient ri = new RecipeIngredient();
                ri.setIngredient(ingredientDTO.getIngredient().toDomainObj());
                ri.setQuantity(ingredientDTO.getQuantity());
                return ri;
            })
            .toList();

        Recipe createdRecipe = recipeInputPort.create(recipe, ingredients);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponseDTO.created(RecipeDTO.from(createdRecipe)));
}

    @Operation(
        summary = "Atualizar uma receita",
        description = "Endpoint para atualização de uma receita.",
        tags = { "Recipe" },
        security = @SecurityRequirement(name = "Auth")
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Receita atualizada com sucesso."
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
                description = "Receita não encontrada.",
                content = @Content(
                    schema = @Schema(implementation = ApiResponseDTO.class)
                )
            ),
            @ApiResponse(
                responseCode = "409",
                description = "Receita já cadastrada.",
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
    public ResponseEntity<ApiResponseDTO<RecipeDTO>> update(
        @Valid
        @RequestBody
        UpdateRecipeDTO updateRecipeDTO,

        HttpServletRequest request
    ) {
        log.info("PUT /api/v1/recipes | Client: {}", request.getRemoteAddr());

        Recipe recipe = recipeInputPort.update(updateRecipeDTO.toDomainObj());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDTO.ok(RecipeDTO.from(recipe)));
    }

    @Operation(
        summary = "Listar receitas",
        description = "Endpoint para listagem de receitas.",
        tags = { "Recipe" },
        security = @SecurityRequirement(name = "Auth")
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Receitas listadas com sucesso."
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
    public ResponseEntity<ApiResponseDTO<List<RecipeDTO>>> findAll(
        @Schema(
            name = "title",
            description = "Título da receita",
            example = "Lasanha"
        )
        @RequestParam(required = false) String title,

        @Schema(
            name = "cookTimeMinutes",
            description = "Tempo de preparo em minutos",
            example = "30"
        )
        @RequestParam(required = false) Integer cookTimeMinutes,

        @Schema(
            name = "servings",
            description = "Quantidade de porções",
            example = "4"
        )
        @RequestParam(required = false) Integer servings,

        @Schema(
            name = "categoryId",
            description = "Id da categoria",
            example = "c5f0f6b3-8a9c-4a1b-8e9c-4a1b8e9c4a1b"
        )
        @RequestParam(required = false) UUID categoryId,

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
        log.info("GET /api/v1/recipes | Client: {}", request.getRemoteAddr());

        List<RecipeDTO> recipes = recipeInputPort.findAll(title, cookTimeMinutes, servings, categoryId, page, size)
            .stream()
            .map(RecipeDTO::from)
            .toList();

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDTO.ok(recipes));
    }

    @Operation(
        summary = "Obter uma receita",
        description = "Endpoint para obter uma receita.",
        tags = { "Recipe" },
        security = @SecurityRequirement(name = "Auth")
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Receita obtida com sucesso."
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
                description = "Receita não encontrada.",
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
    public ResponseEntity<ApiResponseDTO<RecipeDTO>> findById(
        @Schema(
            description = "ID da receita",
            example = "123e4567-e89b-12d3-a456-426614174000"
        )
        @PathVariable UUID id,

        HttpServletRequest request
    ) {
        log.info("GET /api/v1/recipes/{id} | Client: {}", request.getRemoteAddr());

        Recipe recipe = recipeInputPort.findById(id);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDTO.ok(RecipeDTO.from(recipe)));
    }

    @Operation(
        summary = "Excluir uma receita",
        description = "Endpoint para exclusão de uma receita.",
        tags = { "Recipe" },
        security = @SecurityRequirement(name = "Auth")
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "204",
                description = "Receita excluida com sucesso."
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
                description = "Receita não encontrada.",
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
            description = "ID da receita",
            example = "123e4567-e89b-12d3-a456-426614174000"
        )
        @PathVariable UUID id,

        HttpServletRequest request
    ) {
        log.info("DELETE /api/v1/recipes/{id} | Client: {}", request.getRemoteAddr());

        recipeInputPort.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponseDTO.noContent("Receita excluida com sucesso."));
    }
}
