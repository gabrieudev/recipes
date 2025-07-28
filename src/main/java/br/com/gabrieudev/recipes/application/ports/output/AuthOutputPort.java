package br.com.gabrieudev.recipes.application.ports.output;

import java.util.Optional;

import br.com.gabrieudev.recipes.domain.User;

public interface AuthOutputPort {
    Optional<String> generateAccessToken(User user);
    Optional<String> generateRefreshToken(User user);
    boolean isValidToken(String token);
    Optional<User> getUserByToken(String token);
}
