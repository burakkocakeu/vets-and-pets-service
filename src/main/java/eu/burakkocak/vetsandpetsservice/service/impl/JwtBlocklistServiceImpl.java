package eu.burakkocak.vetsandpetsservice.service.impl;

import eu.burakkocak.vetsandpetsservice.service.JwtBlocklistService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class JwtBlocklistServiceImpl implements JwtBlocklistService {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String BLOCKED_TOKENS_KEY = "blockedTokens";

    public JwtBlocklistServiceImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void blockToken(String token, Date expirationDate) {
        long seconds = (expirationDate.getTime() - System.currentTimeMillis()) / 1000;
        redisTemplate.opsForSet().add(BLOCKED_TOKENS_KEY, token);
        redisTemplate.expire(BLOCKED_TOKENS_KEY, seconds, TimeUnit.SECONDS);
    }

    public boolean isTokenBlocked(String token) {
        return redisTemplate.opsForSet().isMember(BLOCKED_TOKENS_KEY, token);
    }
}
