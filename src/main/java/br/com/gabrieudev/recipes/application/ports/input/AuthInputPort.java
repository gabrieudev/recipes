package br.com.gabrieudev.recipes.application.ports.input;

import br.com.gabrieudev.recipes.domain.Tokens;
import br.com.gabrieudev.recipes.domain.User;

public interface AuthInputPort {
    Tokens login(String email, String password);
    Tokens refreshTokens(String refreshToken);
    User getUserByToken(String token);
    void logout(String refreshToken);
    String generateAccessToken(User user);
    String generateRefreshToken(User user);
    boolean isValidToken(String token);
}
