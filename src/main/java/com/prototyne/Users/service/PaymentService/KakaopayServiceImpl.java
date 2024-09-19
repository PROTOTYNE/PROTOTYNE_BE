package com.prototyne.Users.service.PaymentService;

import com.prototyne.Users.service.LoginService.JwtManager;
import com.prototyne.Users.web.dto.KakaoPayDto;
import com.prototyne.domain.enums.TicketOption;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@AllArgsConstructor
public class KakaopayServiceImpl implements KakaopayService {

    private final WebClient webClient;
    private final JwtManager jwtManager;

    @Value("${spring.kakao.pay.admin-key}")
    private String adminKey;

    @Value("${spring.kakao.pay.cid}")
    private String cid;

    public void printKakaoPaySettings() {
        System.out.println("제발 되라ㅋㅋ");
        System.out.println("KakaoPay Admin Key: " + adminKey);
        System.out.println("KakaoPay CID: " + cid);
    }

    @Autowired
    public KakaopayServiceImpl(WebClient.Builder webClientBuilder, JwtManager jwtManager) {
        this.webClient = webClientBuilder.build();
        this.jwtManager = jwtManager;
    }

    @Override
    public KakaoPayDto.KakaoPayReadyResponse readyToPay(String accessToken, TicketOption ticketOption) {
        Long userId = jwtManager.validateJwt(accessToken);

        KakaoPayDto.KakaoPayRequest request = new KakaoPayDto.KakaoPayRequest(userId, ticketOption);
        return webClient.post()
                .uri("https://kapi.kakao.com/v1/payment/ready")
                .header("Authorization", "KakaoAK " + adminKey)
                .header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                .bodyValue(request.toMutliValueMap())
                .retrieve()
                .bodyToMono(KakaoPayDto.KakaoPayReadyResponse.class)
                .block();
    }
}
