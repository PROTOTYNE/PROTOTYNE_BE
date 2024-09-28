package com.prototyne.domain;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "token", timeToLive = 604800)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RefreshToken {
    @Id
    private String id;
    private Long ownerId;
    private String refreshToken;
    private boolean isEnterprise;

    public RefreshToken(Long ownerId,
                        String refreshToken, boolean isEnterprise){
        this.id = generateId(ownerId, isEnterprise);
        this.ownerId = ownerId;
        this.refreshToken = refreshToken;
        this.isEnterprise =isEnterprise;
    }

    public RefreshToken(Long ownerId, String refreshToken){
        this.id = generateId(ownerId, isEnterprise);
    }

    public static String generateId(Long ownerId, boolean isEnterprise){
        return String.valueOf((isEnterprise ? "enterprise: ": "user: ") + ownerId.toString());
    }
}
