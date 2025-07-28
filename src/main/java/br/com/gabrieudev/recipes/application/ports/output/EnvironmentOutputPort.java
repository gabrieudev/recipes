package br.com.gabrieudev.recipes.application.ports.output;

public interface EnvironmentOutputPort {
    String getApiBaseUrl();
    Integer getAccessTokenExpiration();
    Integer getRefreshTokenExpiration();
}
