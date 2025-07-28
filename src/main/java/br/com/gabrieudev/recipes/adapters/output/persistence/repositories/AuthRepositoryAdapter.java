package br.com.gabrieudev.recipes.adapters.output.persistence.repositories;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import br.com.gabrieudev.recipes.adapters.output.persistence.entities.JpaRoleEntity;
import br.com.gabrieudev.recipes.adapters.output.persistence.entities.JpaUserEntity;
import br.com.gabrieudev.recipes.adapters.output.persistence.entities.JpaUserRoleEntity;
import br.com.gabrieudev.recipes.adapters.output.persistence.repositories.jpa.JpaUserRepository;
import br.com.gabrieudev.recipes.adapters.output.persistence.repositories.jpa.JpaUserRoleRepository;
import br.com.gabrieudev.recipes.application.ports.output.AuthOutputPort;
import br.com.gabrieudev.recipes.domain.User;

@Component
public class AuthRepositoryAdapter implements AuthOutputPort {
    private final JpaUserRepository jpaUserRepository;
    private final JpaUserRoleRepository jpaUserRoleRepository;
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    @Value("${spring.application.name}")
    private String issuer;

    public AuthRepositoryAdapter(JpaUserRepository jpaUserRepository, JpaUserRoleRepository jpaUserRoleRepository,
            JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
        this.jpaUserRepository = jpaUserRepository;
        this.jpaUserRoleRepository = jpaUserRoleRepository;
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    public Optional<String> generateAccessToken(User user) {
        try {
            List<String> roles = jpaUserRoleRepository.findByUser(JpaUserEntity.fromDomainObj(user)).stream()
                    .map(JpaUserRoleEntity::getRole)
                    .map(JpaRoleEntity::getName)
                    .toList();

            String scopes = String.join(" ", roles);
            var claims = JwtClaimsSet.builder()
                    .subject(user.getId().toString())
                    .issuer(issuer)
                    .expiresAt(Instant.now().plusSeconds(600))
                    .issuedAt(Instant.now())
                    .claim("firstName", user.getFirstName())
                    .claim("lastName", user.getLastName())
                    .claim("email", user.getEmail())
                    .claim("scope", scopes)
                    .build();

            String accessToken = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

            return Optional.of(accessToken);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<String> generateRefreshToken(User user) {
        try {
            var claims = JwtClaimsSet.builder()
                    .subject(user.getId().toString())
                    .issuer(issuer)
                    .expiresAt(Instant.now().plusSeconds(1296000))
                    .issuedAt(Instant.now())
                    .build();

            String refreshToken = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

            return Optional.of(refreshToken);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> getUserByToken(String token) {
        try {
            var claims = jwtDecoder.decode(token);
            UUID userId = UUID.fromString(claims.getSubject());

            JpaUserEntity user = jpaUserRepository.findById(userId).orElse(null);

            if (user == null) {
                return Optional.empty();
            }

            return Optional.of(user.toDomainObj());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean isValidToken(String token) {
        try {
            jwtDecoder.decode(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
