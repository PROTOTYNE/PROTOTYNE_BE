package com.prototyne.domain;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "token", timeToLive = 604800)
@AllArgsConstructor
@Getter
public class RefreshToken {
    @Id
    private Long id;
    private String refreshToken;
}
