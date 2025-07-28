package br.com.gabrieudev.recipes.application.ports.output;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.gabrieudev.recipes.domain.User;

public interface UserOutputPort {
    Optional<User> create(User user);
    Optional<User> update(User user);
    boolean delete(UUID id);
    Optional<User> findById(UUID id);
    Optional<User> findByEmail(String email);
    boolean existsById(UUID id);
    boolean existsByEmail(String email);
    List<User> findAll(String email, String param, Integer page, Integer size);
    String hashPassword(String password);
    boolean verifyPassword(String rawPassword, String hashPassword);
}
