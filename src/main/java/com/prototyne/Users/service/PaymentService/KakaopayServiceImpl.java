package com.prototyne.Users.service.PaymentService;

import com.prototyne.Users.converter.PaymentConverter;
import com.prototyne.Users.service.LoginService.JwtManager;
import com.prototyne.Users.web.dto.KakaoPayDto;
import com.prototyne.apiPayload.code.status.ErrorStatus;
import com.prototyne.apiPayload.exception.handler.TempHandler;
import com.prototyne.domain.Payment;
import com.prototyne.domain.User;
import com.prototyne.domain.enums.PaymentStatus;
import com.prototyne.domain.enums.TicketOption;
import com.prototyne.repository.PaymentRepository;
import com.prototyne.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class KakaopayServiceImpl implements KakaopayService {

    private final WebClient webClient;
    private final JwtManager jwtManager;
    private final PaymentConverter paymentConverter;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;

    @Value("${spring.kakao.pay.admin-key}")
    private String adminKey;

    @Value("${spring.kakao.pay.cid}")
    private String cid;

    @Value("${spring.server.liveServerIp}")
    private String serverAddress;


    @Autowired
    public KakaopayServiceImpl(WebClient.Builder webClientBuilder, JwtManager jwtManager, PaymentConverter paymentConverter, UserRepository userRepository, PaymentRepository paymentRepository) {
        this.webClient = webClientBuilder.build();
        this.jwtManager = jwtManager;
        this.paymentConverter = paymentConverter;
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public KakaoPayDto.KakaoPayReadyResponse readyToPay(String accessToken, TicketOption ticketOption) {
        Long userId = jwtManager.validateJwt(accessToken);

        KakaoPayDto.KakaoPayRequest request = new KakaoPayDto.KakaoPayRequest(userId, ticketOption);
        String approvalUrl = "http://" + serverAddress + "/payment/success";
        String cancelUrl = "http://" + serverAddress + "/payment/cancel";
        String failUrl = "http://" + serverAddress + "/payment/fail";

        MultiValueMap<String, String> requestBody = request.toMultiValueMap(cid, approvalUrl, cancelUrl, failUrl);
        Payment newPayment = savePaymentLogs(userId, request);

        return webClient.post()
                .uri("https://kapi.kakao.com/v1/payment/ready")
                .header("Authorization", "KakaoAK " + adminKey)
                .header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                .bodyValue(request.toMultiValueMap(cid, approvalUrl, cancelUrl, failUrl))
                .exchangeToMono(response -> {
                    if (response.statusCode().is4xxClientError()) {
                        updatePaymentStatus(newPayment.getOrderId(), PaymentStatus.결제실패);
                        return response.bodyToMono(String.class).map(body -> {
                            throw new RuntimeException("4xx error: " + body);
                        });
                    } else if (response.statusCode().is5xxServerError()) {
                        updatePaymentStatus(newPayment.getOrderId(), PaymentStatus.결제실패);
                        return response.bodyToMono(String.class).map(body -> {
                            throw new RuntimeException("5xx error: " + body);
                        });
                    } else {
                        return response.bodyToMono(KakaoPayDto.KakaoPayReadyResponse.class).map(readyResponse -> {
                            updateToOngoingPaymentLogs(newPayment, readyResponse);
                            return readyResponse;
                        });
                    }
                })
                .block();
    }

    // step1. '결제 대기' 상태의 결제 기록 최초 생성
    private Payment savePaymentLogs(Long userId, KakaoPayDto.KakaoPayRequest req){
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new TempHandler(ErrorStatus.PAYMENT_NO_USER_FOUND));

        Payment payment = paymentConverter.toEntity(user, req);

        return paymentRepository.save(payment);

    }

    // step 2. readyToPay()함수 호출 성공하여 response 제대로 반환 시,
    // 반환되는 제휴사(카카오) 결제 id 결제 db에 저장 후 '결제 진행' 상태로 업데이트
    private void updateToOngoingPaymentLogs(Payment payment, KakaoPayDto.KakaoPayReadyResponse res){
        payment.setOrderId(res.getTid());
        payment.setStatus(PaymentStatus.결제진행);
        paymentRepository.save(payment);
    }



    private void updatePaymentStatus(String orderId, PaymentStatus status){
        Payment originPayment = paymentRepository.findByOrderId(orderId);
        originPayment.setStatus(status);
        paymentRepository.save(originPayment);
    }
}
