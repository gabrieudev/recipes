package br.com.gabrieudev.recipes.adapters.input.rest.controllers;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.gabrieudev.recipes.adapters.input.rest.dtos.ApiResponseDTO;
import br.com.gabrieudev.recipes.adapters.input.rest.dtos.auth.LoginRequest;
import br.com.gabrieudev.recipes.application.ports.input.AuthInputPort;
import br.com.gabrieudev.recipes.domain.Tokens;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {
    private final AuthInputPort authInputPort;
    @Value("${jwt.refresh-token.minutes}")
    private Integer refreshTokenExpiration;
    @Value("${jwt.access-token.minutes}")
    private Integer accessTokenExpiration;

    public AuthController(AuthInputPort authInputPort) {
        this.authInputPort = authInputPort;
    }

    @Operation(
        summary = "Login",
        description = "Endpoint para login de um usuário.",
        tags = { "Auth" }
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Usuário logado com sucesso."
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Usuário ou senha inválidos.",
                content = @Content(
                    schema = @Schema(implementation = ApiResponseDTO.class)
                )
            ),
            @ApiResponse(
                responseCode = "422",
                description = "Usuário ou senha inválidos.",
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
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<String>> login(
        @Valid
        @RequestBody
        LoginRequest loginRequest,

        HttpServletResponse response,

        HttpServletRequest request
    ) {
        log.info("POST /api/v1/auth/login | Client: {}", request.getRemoteAddr());

        Tokens tokens = authInputPort.login(loginRequest.getEmail(), loginRequest.getPassword());

        Cookie refreshTokenCookie = new Cookie("refreshToken", tokens.getRefreshToken());
        refreshTokenCookie.setMaxAge((int) (refreshTokenExpiration * 60));
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");

        Cookie accessTokenCookie = new Cookie("accessToken", tokens.getAccessToken());
        accessTokenCookie.setMaxAge((int) (accessTokenExpiration * 60));
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setPath("/");

        response.addCookie(refreshTokenCookie);
        response.addCookie(accessTokenCookie);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDTO.ok("Login realizado com sucesso."));
    }

    @Operation(
        summary = "Logout",
        description = "Endpoint para logout de um usuário.",
        tags = { "Auth" }
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Usuário deslogado com sucesso."
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Refresh token inválido.",
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
    @PostMapping("/logout")
    public ResponseEntity<ApiResponseDTO<String>> logout(
        HttpServletRequest request,

        HttpServletResponse response
    ) {
        log.info("POST /api/v1/auth/logout | Client: {}", request.getRemoteAddr());

        String refreshToken = Arrays.stream(request.getCookies())
            .filter(cookie -> "refreshToken".equals(cookie.getName()))
            .findFirst()
            .map(Cookie::getValue)
            .orElse(null);

        authInputPort.logout(refreshToken);

        Cookie refreshTokenCookie = new Cookie("refreshToken", "");
        refreshTokenCookie.setMaxAge(0);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");

        Cookie accessTokenCookie = new Cookie("accessToken", "");
        accessTokenCookie.setMaxAge(0);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setPath("/");

        response.addCookie(refreshTokenCookie);
        response.addCookie(accessTokenCookie);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDTO.ok("Logout realizado com sucesso."));
    }

    @Operation(
        summary = "Atualizar tokens",
        description = "Endpoint para atualizar os tokens.",
        tags = { "Auth" }
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Tokens atualizados com sucesso."
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Refresh token inválido.",
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
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponseDTO<String>> refresh(
        HttpServletRequest request,

        HttpServletResponse response
    ) {
        log.info("POST /api/v1/auth/refresh | Client: {}", request.getRemoteAddr());

        String refreshToken = Arrays.stream(request.getCookies())
            .filter(cookie -> "refreshToken".equals(cookie.getName()))
            .findFirst()
            .map(Cookie::getValue)
            .orElse(null);

        Tokens tokens = authInputPort.refreshTokens(refreshToken);

        Cookie refreshTokenCookie = new Cookie("refreshToken", tokens.getRefreshToken());
        refreshTokenCookie.setMaxAge((int) (refreshTokenExpiration * 60));
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");

        Cookie accessTokenCookie = new Cookie("accessToken", tokens.getAccessToken());
        accessTokenCookie.setMaxAge((int) (accessTokenExpiration * 60));
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setPath("/");

        response.addCookie(refreshTokenCookie);
        response.addCookie(accessTokenCookie);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDTO.ok("Tokens atualizados com sucesso."));
    }
}
