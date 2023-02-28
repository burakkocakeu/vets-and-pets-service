package eu.burakkocak.vetsandpetsservice.service.impl;

import eu.burakkocak.vetsandpetsservice.service.CacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CacheServiceImpl implements CacheService {
    private final RedisTemplate<String, Boolean> redisTemplate;

    @Override
    public boolean getBlackListedToken(String token) {
        ValueOperations<String, Boolean> ops = redisTemplate.opsForValue();
        return ops.get(token);
    }

    @Override
    public void setBlackListToken(String token) {
        ValueOperations<String, Boolean> ops = redisTemplate.opsForValue();
        ops.set(token, true);
    }
}
