package com.nus.nexchange.userservice.application.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    public void saveToken(String token, String username, long expirationTime) {
        redisTemplate.opsForValue().set(token, username, expirationTime, TimeUnit.MILLISECONDS);
    }

    public String getUsernameFromToken(String token) {
        return redisTemplate.opsForValue().get(token);
    }

    public void deleteToken(String token) {
        redisTemplate.delete(token);
    }

    public boolean isTokenPresent(String token) {
        return redisTemplate.hasKey(token);
    }
}
