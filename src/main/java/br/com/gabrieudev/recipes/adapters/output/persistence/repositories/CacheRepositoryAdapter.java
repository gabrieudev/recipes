package br.com.gabrieudev.recipes.adapters.output.persistence.repositories;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import br.com.gabrieudev.recipes.application.ports.output.CacheOutputPort;

@Component
public class CacheRepositoryAdapter implements CacheOutputPort {
    private final RedisTemplate<String, Object> redisTemplate;

    public CacheRepositoryAdapter(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean delete(String key) {
        try {
            redisTemplate.delete(key);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Optional<String> get(String key) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value instanceof String) {
            return Optional.of((String) value);
        }
        return Optional.empty();
    }

    @Override
    public boolean set(String key, String value, Integer minutesToLive) {
        try {
            redisTemplate.opsForValue().set(key, value, minutesToLive, TimeUnit.MINUTES);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }
}
