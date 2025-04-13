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
    @Value("${KAKAO_PAY_SECRET_KEY}")
    private String secretKey;

    @Value("${KAKAO_PAY_CID}")
    private String cid;

    @Value("${spring.server.liveServerIp:13.125.19.26}")
    private String serverAddress;
}
