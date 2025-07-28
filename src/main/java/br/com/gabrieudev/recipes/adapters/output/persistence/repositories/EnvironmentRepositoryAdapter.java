package br.com.gabrieudev.recipes.adapters.output.persistence.repositories;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.com.gabrieudev.recipes.application.ports.output.EnvironmentOutputPort;

@Component
public class EnvironmentRepositoryAdapter implements EnvironmentOutputPort {
    @Value("${api.base-url}")
    private String apiBaseUrl;
    @Value("${jwt.access-token.minutes}")
    private Integer accessTokenExpiration;
    @Value("${jwt.refresh-token.minutes}")
    private Integer refreshTokenExpiration;
    
    @Override
    public String getApiBaseUrl() {
        return apiBaseUrl;
    }

    @Override
    public Integer getAccessTokenExpiration() {
        return accessTokenExpiration;
    }

    @Override
    public Integer getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }
}
