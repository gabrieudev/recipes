package br.com.gabrieudev.recipes.adapters.output.persistence.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import br.com.gabrieudev.recipes.adapters.output.persistence.entities.JpaUserEntity;
import br.com.gabrieudev.recipes.adapters.output.persistence.repositories.jpa.JpaUserRepository;
import br.com.gabrieudev.recipes.application.ports.output.UserOutputPort;
import br.com.gabrieudev.recipes.domain.User;

@Component
public class UserRepositoryAdapter implements UserOutputPort {
    private final JpaUserRepository jpaUserRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserRepositoryAdapter(JpaUserRepository jpaUserRepository, BCryptPasswordEncoder passwordEncoder) {
        this.jpaUserRepository = jpaUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> create(User user) {
        try {
            JpaUserEntity savedUser = jpaUserRepository.save(JpaUserEntity.fromDomainObj(user));

            return Optional.of(savedUser.toDomainObj());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean delete(UUID id) {
        try {
            jpaUserRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaUserRepository.existsByEmail(email);
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaUserRepository.existsById(id);
    }

    @Override
    public List<User> findAll(String param, String email, Integer page, Integer size) {
        return jpaUserRepository.findAll(param, email, PageRequest.of(page, size))
                .stream()
                .map(JpaUserEntity::toDomainObj)
                .toList();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaUserRepository.findByEmail(email)
                .map(JpaUserEntity::toDomainObj);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpaUserRepository.findById(id)
                .map(JpaUserEntity::toDomainObj);
    }

    @Override
    public String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public Optional<User> update(User user) {
        try {
            JpaUserEntity userToUpdate = jpaUserRepository.findById(user.getId()).orElse(null);

            if (userToUpdate == null) {
                return Optional.empty();
            }

            userToUpdate.update(user);

            return Optional.of(jpaUserRepository.save(userToUpdate).toDomainObj());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean verifyPassword(String rawPassword, String hashPassword) {
        return passwordEncoder.matches(rawPassword, hashPassword);
    }
}
