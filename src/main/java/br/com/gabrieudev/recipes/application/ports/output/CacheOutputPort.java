package br.com.gabrieudev.recipes.application.ports.output;

import java.util.Optional;

public interface CacheOutputPort {
    boolean delete(String key);
    Optional<String> get(String key);
    boolean set(String key, String value, Integer minutesToLive);
    boolean hasKey(String key);
}
