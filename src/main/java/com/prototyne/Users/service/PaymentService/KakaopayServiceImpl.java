package com.prototyne.Users.service.PaymentService;

import com.prototyne.Users.converter.PaymentConverter;
import com.prototyne.config.JwtManager;
import com.prototyne.Users.service.TicketService.TicketService;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KakaopayServiceImpl implements KakaopayService {

    private final WebClient webClient;
    private final JwtManager jwtManager;
    private final PaymentConverter paymentConverter;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final KakaopayProperties kakaopayProperties;
    private final TicketService ticketService;

    private RestTemplate restTemplate = new RestTemplate();
    private KakaoPayDto.KakaoPayReadyResponse kakaoReady;

    private HttpHeaders getHeaders(){
        HttpHeaders headers = new HttpHeaders();
        String auth = "SECRET_KEY " + kakaopayProperties.getSecretKey();
        headers.set("Authorization", auth);
        headers.set("Content-Type", "application/json");
        return headers;
    }
    @Autowired
    public KakaopayServiceImpl(WebClient.Builder webClientBuilder, JwtManager jwtManager, PaymentConverter paymentConverter, UserRepository userRepository, PaymentRepository paymentRepository, KakaopayProperties kakaopayProperties, TicketService ticketService) {
        this.webClient = webClientBuilder.build();
        this.jwtManager = jwtManager;
        this.paymentConverter = paymentConverter;
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
        this.kakaopayProperties = kakaopayProperties;
        this.ticketService = ticketService;
    }

    // 1.결제 시작-> kakaoPay 서버에게 결제 준비 요청 -> 결제 성공 여부 확인
    @Override
    public KakaoPayDto.KakaoPayReadyResponse readyToPay(String accessToken, TicketOption ticketOption) {
        Long userId = jwtManager.validateJwt(accessToken);

        KakaoPayDto.KakaoPayRequest readyReq = new KakaoPayDto.KakaoPayRequest(userId, ticketOption);
        String approvalUrl = "http://" + kakaopayProperties.getServerAddress() + "/payment/success";
        String cancelUrl = "http://" + kakaopayProperties.getServerAddress() + "/payment/cancel";
        String failUrl = "http://" + kakaopayProperties.getServerAddress() + "/payment/fail";

        Payment newPayment = savePaymentLogs(userId, readyReq);

        Map<String, Object> parameters = readyReq.toFormData(kakaopayProperties.getCid(), approvalUrl, cancelUrl, failUrl);

        HttpEntity<Map<String, Object>> reqEntity = new HttpEntity<>(parameters, this.getHeaders());

        RestTemplate restTemplate = new RestTemplate();

        kakaoReady = restTemplate.postForObject(
                "https://open-api.kakaopay.com/online/v1/payment/ready",
                reqEntity,
                KakaoPayDto.KakaoPayReadyResponse.class);
        if(kakaoReady != null) {
            updatePaymentStatus(newPayment.getId(), PaymentStatus.결제진행);
            newPayment.setTid(kakaoReady.getTid());
            paymentRepository.save(newPayment);
        }
        else {
            updatePaymentStatus(newPayment.getId(), PaymentStatus.결제실패);
            throw new TempHandler(ErrorStatus.PAYMENT_READY_SERVER_FAILURE);
        }
        return kakaoReady;

    }

    // 2. 결제 완료 -> kakaoPay 서버에게 결제 승인 요청
    public KakaoPayDto.KakaoPayApproveResponse approvePayment(String accessToken, String pgToken) {
        Long userId = jwtManager.validateJwt(accessToken);
        Payment payment = paymentRepository.findByUserIdAndTid(userId, kakaoReady.getTid());

        if(payment == null){
            throw new TempHandler(ErrorStatus.PAYMENT_NO_ORDER_FOUND);
        }

        Map<String, String> parameters = new HashMap<>();
        parameters.put("cid", kakaopayProperties.getCid());
        parameters.put("tid", kakaoReady.getTid());
        parameters.put("partner_order_id", payment.getPartnerOrderId());
        parameters.put("partner_user_id", payment.getUser().getId().toString());
        parameters.put("pg_token", pgToken);

        HttpEntity<Map<String, String>> reqEntity = new HttpEntity<>(parameters, this.getHeaders());
        System.out.println();
        System.out.println();
        System.out.println(reqEntity);
        System.out.println();
        System.out.println();

        if (pgToken == null || pgToken.isEmpty()) {
            throw new TempHandler(ErrorStatus.PAYMENT_INVALID_PGTOKEN);
        }

        RestTemplate restTemplate = new RestTemplate();

        KakaoPayDto.KakaoPayApproveResponse approveResponse = restTemplate.postForObject(
                "https://open-api.kakaopay.com/online/v1/payment/approve",
                reqEntity,
                KakaoPayDto.KakaoPayApproveResponse.class);
        if(approveResponse != null){
            updatePaymentStatus(payment.getId(), PaymentStatus.결제성공);
            ticketService.buyTicket(accessToken, payment.getTicketOption().getTicketNumber());
        }
        else {
            updatePaymentStatus(payment.getId(), PaymentStatus.결제실패);
        }
        return approveResponse;
    }


    // step1. '결제 대기' 상태의 결제 기록 최초 생성
    private Payment savePaymentLogs(Long userId, KakaoPayDto.KakaoPayRequest req){
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new TempHandler(ErrorStatus.PAYMENT_NO_USER_FOUND));

        Payment payment = paymentConverter.toEntity(user, req);

        return paymentRepository.save(payment);

    }


    private void updatePaymentStatus(Long paymentId, PaymentStatus status){
        Payment originPayment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new TempHandler(ErrorStatus.PAYMENT_NO_ORDER_FOUND));
        originPayment.setStatus(status);
        paymentRepository.save(originPayment);
    }

}
