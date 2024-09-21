package com.prototyne.Users.service.PaymentService;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "kakaopay")
@Getter
@Setter
public class KakaopayProperties {
    @Value("${spring.kakao.pay.secret-key}")
    private String secretKey;

    @Value("${spring.kakao.pay.cid}")
    private String cid;

    @Value("${spring.server.liveServerIp}")
    private String serverAddress;
}
