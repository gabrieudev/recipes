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
import br.com.gabrieudev.recipes.adapters.input.rest.dtos.category.CategoryDTO;
import br.com.gabrieudev.recipes.adapters.input.rest.dtos.category.CreateCategoryDTO;
import br.com.gabrieudev.recipes.adapters.input.rest.dtos.category.UpdateCategoryDTO;
import br.com.gabrieudev.recipes.application.ports.input.CategoryInputPort;
import br.com.gabrieudev.recipes.domain.Category;
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
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryInputPort categoryInputPort;

    public CategoryController(CategoryInputPort categoryInputPort) {
        this.categoryInputPort = categoryInputPort;
    }

    @Operation(
        summary = "Criar uma categoria",
        description = "Endpoint para criação de uma categoria.",
        tags = { "Category" },
        security = @SecurityRequirement(name = "Auth")
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "201",
                description = "Categoria criado com sucesso."
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
                description = "Categoria já criada.",
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
    public ResponseEntity<ApiResponseDTO<CategoryDTO>> create(
        @Valid
        @RequestBody
        CreateCategoryDTO createCategoryDTO,

        HttpServletRequest request
    ) {
        log.info("POST /api/v1/categories | Client: {}", request.getRemoteAddr());

        Category category = categoryInputPort.create(createCategoryDTO.toDomainObj());

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDTO.created(CategoryDTO.from(category)));
    }

    @Operation(
        summary = "Atualizar uma categoria",
        description = "Endpoint para atualização de uma categoria.",
        tags = { "Category" },
        security = @SecurityRequirement(name = "Auth")
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Categoria atualizada com sucesso."
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
                description = "Categoria não encontrada.",
                content = @Content(
                    schema = @Schema(implementation = ApiResponseDTO.class)
                )
            ),
            @ApiResponse(
                responseCode = "409",
                description = "Categoria já cadastrada.",
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
    public ResponseEntity<ApiResponseDTO<CategoryDTO>> update(
        @Valid
        @RequestBody
        UpdateCategoryDTO updateCategoryDTO,

        HttpServletRequest request
    ) {
        log.info("PUT /api/v1/categories | Client: {}", request.getRemoteAddr());

        Category category = categoryInputPort.update(updateCategoryDTO.toDomainObj());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDTO.ok(CategoryDTO.from(category)));
    }

    @Operation(
        summary = "Listar categorias",
        description = "Endpoint para listagem de categorias.",
        tags = { "Category" },
        security = @SecurityRequirement(name = "Auth")
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Categorias listadas com sucesso."
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
    public ResponseEntity<ApiResponseDTO<List<CategoryDTO>>> findAll(
        @Schema(
            name = "name",
            description = "Nome da categoria",
            example = "Massas"
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
        log.info("GET /api/v1/categories | Client: {}", request.getRemoteAddr());

        List<CategoryDTO> categories = categoryInputPort.findAll(name, page, size)
            .stream()
            .map(CategoryDTO::from)
            .toList();

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDTO.ok(categories));
    }

    @Operation(
        summary = "Obter uma categoria",
        description = "Endpoint para obter uma categoria.",
        tags = { "Category" },
        security = @SecurityRequirement(name = "Auth")
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Categoria obtida com sucesso."
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
                description = "Categoria não encontrada.",
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
    public ResponseEntity<ApiResponseDTO<CategoryDTO>> findById(
        @Schema(
            description = "ID da categoria",
            example = "123e4567-e89b-12d3-a456-426614174000"
        )
        @PathVariable UUID id,

        HttpServletRequest request
    ) {
        log.info("GET /api/v1/categories/{id} | Client: {}", request.getRemoteAddr());

        Category category = categoryInputPort.findById(id);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDTO.ok(CategoryDTO.from(category)));
    }

    @Operation(
        summary = "Excluir uma categoria",
        description = "Endpoint para exclusão de uma categoria.",
        tags = { "Category" },
        security = @SecurityRequirement(name = "Auth")
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "204",
                description = "Categoria excluida com sucesso."
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
                description = "Categoria não encontrada.",
                content = @Content(
                    schema = @Schema(implementation = ApiResponseDTO.class)
                )
            ),
            @ApiResponse(
                responseCode = "409",
                description = "Categoria possui receitas associadas.",
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
            description = "ID da categoria",
            example = "123e4567-e89b-12d3-a456-426614174000"
        )
        @PathVariable UUID id,

        HttpServletRequest request
    ) {
        log.info("DELETE /api/v1/categories/{id} | Client: {}", request.getRemoteAddr());

        categoryInputPort.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponseDTO.noContent("Categoria excluida com sucesso."));
    }
}
