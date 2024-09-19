package com.prototyne.Users.service.PaymentService;

import com.prototyne.Users.service.LoginService.JwtManager;
import com.prototyne.Users.web.dto.KakaoPayDto;
import com.prototyne.domain.enums.TicketOption;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
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

    @Value("${spring.server.liveServerIp}")
    private String serverAddress;


    @Autowired
    public KakaopayServiceImpl(WebClient.Builder webClientBuilder, JwtManager jwtManager) {
        this.webClient = webClientBuilder.build();
        this.jwtManager = jwtManager;
    }

    @Override
    public KakaoPayDto.KakaoPayReadyResponse readyToPay(String accessToken, TicketOption ticketOption) {
        Long userId = jwtManager.validateJwt(accessToken);

        KakaoPayDto.KakaoPayRequest request = new KakaoPayDto.KakaoPayRequest(userId, ticketOption);
        String approvalUrl = "http://" + serverAddress + "/payment/success";
        String cancelUrl = "http://" + serverAddress + "/payment/cancel";
        String failUrl = "http://" + serverAddress + "/payment/fail";

        MultiValueMap<String, String> requestBody = request.toMultiValueMap(cid, approvalUrl, cancelUrl, failUrl);
        System.out.println("Request Body: " + requestBody);

        return webClient.post()
                .uri("https://kapi.kakao.com/v1/payment/ready")
                .header("Authorization", "KakaoAK " + adminKey)
                .header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                .bodyValue(request.toMultiValueMap(cid, approvalUrl, cancelUrl, failUrl))
                .exchangeToMono(response -> {
                    if (response.statusCode().is4xxClientError()) {
                        // 클라이언트 오류 로그 출력
                        return response.bodyToMono(String.class).map(body -> {
                            throw new RuntimeException("4xx error: " + body);
                        });
                    } else if (response.statusCode().is5xxServerError()) {
                        // 서버 오류 로그 출력
                        return response.bodyToMono(String.class).map(body -> {
                            throw new RuntimeException("5xx error: " + body);
                        });
                    } else {
                        // 정상 응답 처리
                        return response.bodyToMono(KakaoPayDto.KakaoPayReadyResponse.class);
                    }
                })
                .block();
    }
}
