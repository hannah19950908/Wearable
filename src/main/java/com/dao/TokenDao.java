package com.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

/**
 * Created by 63289 on 2017/4/27.
 */
@Repository
public class TokenDao {
    private final RedisTemplate<String, String> redisTemplate;
    private final static long validTime=1;
    private final static long cacheLiveTime=10;

    @Autowired
    public TokenDao(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void put(String token, String accountNumber) {
        redisTemplate.boundValueOps(token).set(accountNumber,validTime,TimeUnit.HOURS);
    }

    public String get(String token) {
        return redisTemplate.opsForValue().get(token);
    }

    public Boolean hasCache(String accountNumber){
        if(redisTemplate.opsForValue().get(accountNumber)==null){
            redisTemplate.boundValueOps(accountNumber).set("true",cacheLiveTime,TimeUnit.MINUTES);
            return false;
        }
        return true;
    }

    public void delete(String token){
        redisTemplate.delete(token);
    }
}
