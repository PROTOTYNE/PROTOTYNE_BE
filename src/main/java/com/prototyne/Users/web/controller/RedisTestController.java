package com.prototyne.Users.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisTestController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/test-redis")
    public String testRedis() {
        // Redis에 데이터 저장
        redisTemplate.opsForValue().set("testKey", "Hello, Redis!");

        // Redis에서 데이터 가져오기
        String value = (String) redisTemplate.opsForValue().get("testKey");

        return "Redis에 저장된 값: " + value;
    }
}
