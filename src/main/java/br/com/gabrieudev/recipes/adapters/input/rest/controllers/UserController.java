package br.com.gabrieudev.recipes.adapters.input.rest.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
import br.com.gabrieudev.recipes.adapters.input.rest.dtos.user.CreateUserDTO;
import br.com.gabrieudev.recipes.adapters.input.rest.dtos.user.ResetPasswordDTO;
import br.com.gabrieudev.recipes.adapters.input.rest.dtos.user.UpdateUserDTO;
import br.com.gabrieudev.recipes.adapters.input.rest.dtos.user.UserDTO;
import br.com.gabrieudev.recipes.application.ports.input.UserInputPort;
import br.com.gabrieudev.recipes.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserController {
    private final UserInputPort userInputPort;
    @Value("${frontend.base-url}")
    private String frontendUrl;
    
    public UserController(UserInputPort userInputPort) {
        this.userInputPort = userInputPort;
    }

    @Operation(
        summary = "Cadastrar usuário",
        description = "Endpoint para cadastro de um usuário.",
        tags = { "User" }
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "201",
                description = "Usuário cadastrado com sucesso."
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
                description = "Usuário já cadastrado.",
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
    @PostMapping
    public ResponseEntity<ApiResponseDTO<UserDTO>> create(
        @Valid
        @RequestBody 
        CreateUserDTO user,

        HttpServletRequest request
    ) {
        log.info("POST /api/v1/users | Client: {}", request.getRemoteAddr());

        User createdUser = userInputPort.create(user.toDomainObj());

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDTO.created(UserDTO.from(createdUser)));
    }

    @Operation(
        summary = "Atualizar usuário",
        description = "Endpoint para atualização de um usuário.",
        tags = { "User" },
        security = @SecurityRequirement(name = "Auth")
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Usuário atualizado com sucesso."
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
                description = "Usuário não encontrado.",
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
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    @PutMapping
    public ResponseEntity<ApiResponseDTO<UserDTO>> update(
        @Valid
        @RequestBody 
        UpdateUserDTO user,

        HttpServletRequest request
    ) {
        log.info("PUT /api/v1/users | Client: {}", request.getRemoteAddr());

        User updatedUser = userInputPort.update(user.toDomainObj());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDTO.ok(UserDTO.from(updatedUser)));
    }

    @Operation(
        summary = "Listar usuários",
        description = "Endpoint para listagem de usuários.",
        tags = { "User" },
        security = @SecurityRequirement(name = "Auth")
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Usuários listados com sucesso."
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
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponseDTO<Page<UserDTO>>> findAll(
        @Schema(
            name = "email",
            description = "E-mail do usuário",
            example = "joao@gmail.com"
        )
        @RequestParam(required = false) String email,
        
        @Schema(
            name = "param",
            description = "Parâmetro de busca",
            example = "Maria"
        )
        @RequestParam(required = false) String param,
        
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
        log.info("GET /api/v1/users | Client: {}", request.getRemoteAddr());

        List<UserDTO> users = userInputPort.findAll(param, email, page, size)
            .stream()
            .map(UserDTO::from)
            .toList();

        Page<UserDTO> usersPage = new PageImpl<>(users, PageRequest.of(page, size), size);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDTO.ok(usersPage));
    }

    @Operation(
        summary = "Obter usuário logado",
        description = "Endpoint para obter o usuário logado.",
        tags = { "User" },
        security = @SecurityRequirement(name = "Auth")
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Usuário logado com sucesso."
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Token inválido.",
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
    @GetMapping("/me")
    public ResponseEntity<ApiResponseDTO<UserDTO>> getMe(
        HttpServletRequest request
    ) {
        log.info("GET /api/v1/users/me | Client: {}", request.getRemoteAddr());

        String accessToken = Arrays.stream(request.getCookies())
            .filter(cookie -> "accessToken".equals(cookie.getName()))
            .findFirst()
            .map(Cookie::getValue)
            .orElse(null);

        User user = userInputPort.getMe(accessToken);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDTO.ok(UserDTO.from(user)));
    }

    @Operation(
        summary = "Obter usuário",
        description = "Endpoint para obter um usuário.",
        tags = { "User" },
        security = @SecurityRequirement(name = "Auth")
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Usuário obtido com sucesso."
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
                description = "Usuário não encontrado.",
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
    public ResponseEntity<ApiResponseDTO<UserDTO>> findById(
        @Schema(
            description = "ID do usuário",
            example = "123e4567-e89b-12d3-a456-426614174000"
        )
        @PathVariable UUID id,

        HttpServletRequest request
    ) {
        log.info("GET /api/v1/users/{id} | Client: {}", request.getRemoteAddr());

        User user = userInputPort.findById(id);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDTO.ok(UserDTO.from(user)));
    }

    @Operation(
        summary = "Confirmar e-mail",
        description = "Endpoint para confirmar e-mail.",
        tags = { "User" }
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "E-mail confirmado com sucesso.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                        implementation = Void.class
                    )
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Código de confirmação inválido.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                        implementation = ApiResponseDTO.class
                    )
                )
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Erro interno do servidor.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                        implementation = ApiResponseDTO.class
                    )
                )
            )
        }
    )
    @GetMapping("/confirm")
    public void confirmEmail(
        @Schema(
            name = "code",
            description = "Código de confirmação",
            example = "123e4567-e89b-12d3-a456-426614174000"
        )
        @RequestParam(required = true) UUID code,

        HttpServletResponse response,

        HttpServletRequest request
    ) {
        log.info("GET /api/v1/users/confirm | Client: {}", request.getRemoteAddr());

        userInputPort.confirmEmail(code);
        
        response.setHeader("Location", frontendUrl);
        response.setStatus(HttpStatus.FOUND.value());
    }

    @Operation(
        summary = "Enviar e-mail de confirmação",
        description = "Endpoint para enviar e-mail de confirmação.",
        tags = { "User" }
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "E-mail enviado com sucesso."
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Código de confirmação invático.",
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
    @PostMapping("/{id}/email")
    public ResponseEntity<ApiResponseDTO<String>> sendConfirmationEmail(
        @Schema(
            description = "ID do usuário",
            example = "123e4567-e89b-12d3-a456-426614174000"
        )
        @PathVariable UUID id,

        HttpServletRequest request
    ) {
        log.info("POST /api/v1/users/{id}/email | Client: {}", request.getRemoteAddr());

        userInputPort.sendConfirmationEmail(id);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDTO.ok("E-mail enviado com sucesso."));
    }

    @Operation(
        summary = "Deletar usuário",
        description = "Endpoint para deletar um usuário.",
        tags = { "User" },
        security = @SecurityRequirement(name = "Auth")
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "204",
                description = "Usuário deletado com sucesso."
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
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<String>> delete(
        @Schema(
            description = "ID do usuário",
            example = "123e4567-e89b-12d3-a456-426614174000"
        )
        @PathVariable UUID id,

        HttpServletRequest request
    ) {
        log.info("DELETE /api/v1/users/{id} | Client: {}", request.getRemoteAddr());

        userInputPort.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponseDTO.noContent("Usuário deletado com sucesso."));
    }

    @Operation(
        summary = "Enviar e-mail de redefinição de senha",
        description = "Endpoint para enviar e-mail de redefinição de senha.",
        tags = { "User" },
        security = @SecurityRequirement(name = "Auth")
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "E-mail enviado com sucesso."
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Código de confirmação invático.",
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
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponseDTO<String>> resetPassword(
        HttpServletRequest request,    

        HttpServletResponse response
    ) {
        log.info("POST /api/v1/users/forgot-password | Client: {}", request.getRemoteAddr());

        String accessToken = Arrays.stream(request.getCookies())
            .filter(cookie -> "accessToken".equals(cookie.getName()))
            .findFirst()
            .map(Cookie::getValue)
            .orElse(null);

        userInputPort.sendResetPasswordEmail(accessToken);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDTO.ok("E-mail enviado com sucesso."));
    }

    @Operation(
        summary = "Validar redefinição de senha",
        description = "Endpoint para validar redefinição de senha.",
        tags = { "User" }
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Usuário redirecionado para redefinição de senha."
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Código de confirmação invático.",
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
    @GetMapping("/validate-reset-password")
    public void validateResetPassword(
        @Schema(
            name = "code",
            description = "Código de confirmação",
            example = "123e4567-e89b-12d3-a456-426614174000"
        )
        @RequestParam(required = true) UUID code,

        HttpServletResponse response,

        HttpServletRequest request
    ) {
        log.info("GET /api/v1/users/validate-reset-password | Client: {}", request.getRemoteAddr());

        userInputPort.validateResetPassword(code);

        response.setHeader("Location", frontendUrl + "/reset-password?code=" + code);
        response.setStatus(HttpStatus.FOUND.value());
    }

    @Operation(
        summary = "Redefinir senha",
        description = "Endpoint para redefinir senha.",
        tags = { "User" }
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Senha redefinida com sucesso."
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Código de confirmação invático.",
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
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponseDTO<String>> resetPassword(
        @Valid
        @RequestBody
        ResetPasswordDTO resetPasswordDTO,

        HttpServletRequest request
    ) {
        log.info("POST /api/v1/users/reset-password | Client: {}", request.getRemoteAddr());

        userInputPort.resetPassword(resetPasswordDTO.getCode(), resetPasswordDTO.getPassword());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDTO.ok("Senha redefinida com sucesso."));
    }
}
