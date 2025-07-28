package br.com.gabrieudev.recipes.application.services;

import br.com.gabrieudev.recipes.application.exceptions.BadCredentialsException;
import br.com.gabrieudev.recipes.application.exceptions.InternalErrorException;
import br.com.gabrieudev.recipes.application.exceptions.InvalidTokenException;
import br.com.gabrieudev.recipes.application.ports.input.AuthInputPort;
import br.com.gabrieudev.recipes.application.ports.output.AuthOutputPort;
import br.com.gabrieudev.recipes.application.ports.output.CacheOutputPort;
import br.com.gabrieudev.recipes.application.ports.output.EnvironmentOutputPort;
import br.com.gabrieudev.recipes.application.ports.output.UserOutputPort;
import br.com.gabrieudev.recipes.domain.Tokens;
import br.com.gabrieudev.recipes.domain.User;

public class AuthService implements AuthInputPort {
    private final AuthOutputPort authOutputPort;
    private final UserOutputPort userOutputPort;
    private final CacheOutputPort cacheOutputPort;
    private final EnvironmentOutputPort environmentOutputPort;

    public AuthService(AuthOutputPort authOutputPort, UserOutputPort userOutputPort, CacheOutputPort cacheOutputPort, EnvironmentOutputPort environmentOutputPort) {
        this.authOutputPort = authOutputPort;
        this.userOutputPort = userOutputPort;
        this.cacheOutputPort = cacheOutputPort;
        this.environmentOutputPort = environmentOutputPort;
    }

    @Override
    public Tokens login(String email, String password) {
        User user = userOutputPort.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Usuário ou senha incorretos."));

        if (!userOutputPort.verifyPassword(password, user.getPassword())) {
            throw new BadCredentialsException("Usuário ou senha incorretos.");
        }

        String accessToken = generateAccessToken(user);
        String refreshToken = generateRefreshToken(user);

        return new Tokens(accessToken, refreshToken);
    }

    @Override
    public Tokens refreshTokens(String refreshToken) {
        if (!isValidToken(refreshToken)) {
            throw new InvalidTokenException("Token inválido.");
        }

        User user = getUserByToken(refreshToken);

        String newAccessToken = generateAccessToken(user);
        String newRefreshToken = generateRefreshToken(user);

        return new Tokens(newAccessToken, newRefreshToken);
    }

    @Override
    public void logout(String refreshToken) {
        if (!isValidToken(refreshToken)) {
            throw new InvalidTokenException("Token inválido.");
        }

        User user = getUserByToken(refreshToken);

        cacheOutputPort.delete("accessToken:" + user.getId().toString());
        cacheOutputPort.delete("refreshToken:" + user.getId().toString());
    }

    @Override
    public User getUserByToken(String token) {
        if (!authOutputPort.isValidToken(token)) {
            throw new InvalidTokenException("Token inválido.");
        }

        return authOutputPort.getUserByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Token inválido."));
    }

    @Override
    public String generateAccessToken(User user) {
        String accessToken = authOutputPort.generateAccessToken(user)
                .orElseThrow(() -> new InternalErrorException("Erro ao gerar token de acesso."));

        cacheOutputPort.delete("accessToken:" + user.getId().toString());

        cacheOutputPort.set("accessToken:" + user.getId().toString(), accessToken, environmentOutputPort.getAccessTokenExpiration());

        return accessToken;
    }

    @Override
    public String generateRefreshToken(User user) {
        String refreshToken = authOutputPort.generateRefreshToken(user)
                .orElseThrow(() -> new InternalErrorException("Erro ao gerar token de acesso."));

        cacheOutputPort.delete("refreshToken:" + user.getId().toString());

        cacheOutputPort.set("refreshToken:" + user.getId().toString(), refreshToken, environmentOutputPort.getRefreshTokenExpiration());

        return refreshToken;
    }

    @Override
    public boolean isValidToken(String token) {
        User user = getUserByToken(token);

        return cacheOutputPort.hasKey("accessToken:" + user.getId().toString()) || cacheOutputPort.hasKey("refreshToken:" + user.getId().toString());
    }
}
