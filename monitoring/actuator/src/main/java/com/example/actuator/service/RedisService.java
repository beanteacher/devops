package com.example.actuator.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RedisService {
    private final RedisTemplate<String,Object> redisTemplate;

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveJson(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void saveMap(String key, Map<String, Object> map) {
        redisTemplate.opsForValue().set(key, map);
    }

    public String readJson(String key) {
        Object str = redisTemplate.opsForValue().get(key);
        return (str instanceof String) ? str.toString() : null;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> readMap(String key) {
        Object m = redisTemplate.opsForValue().get(key);
        return (m instanceof Map) ? (Map<String, Object>) m : null;
    }
}
