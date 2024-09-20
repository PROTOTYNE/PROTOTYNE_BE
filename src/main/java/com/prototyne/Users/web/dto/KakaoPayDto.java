package com.prototyne.Users.web.dto;

import com.prototyne.domain.enums.TicketOption;
import lombok.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.UUID;

public class KakaoPayDto {

    @Getter
    @Setter
    public static class KakaoPayReadyResponse{
        private String tid;
        private String next_redirect_pc_url;
        private String created_at;
    }

    @Data
    public static class KakaoPayRequest {
        private String partnerOrderId; // 결제 건 고유 번호
        private Long userId;
        private String itemName;
        private int quantity;
        private int totalAmount;
        private String ticketOption;


        public KakaoPayRequest(Long userId, TicketOption ticketOption){
            this.partnerOrderId = UUID.randomUUID().toString();
            this.userId = userId;
            this.itemName = "[프로토타인] "+ticketOption.getTicketNumber() + " 티켓 구매";
            this.quantity = 1;
            this.totalAmount = ticketOption.getPrice();
            this.ticketOption = String.valueOf(ticketOption);
        }

        public MultiValueMap<String, String> toMultiValueMap(String cid, String approvalUrl, String cancelUrl, String failUrl) {
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("cid", cid);  // 테스트용 CID
            map.add("partner_order_id", partnerOrderId);
            map.add("partner_user_id", userId.toString());
            map.add("item_name", itemName);
            map.add("quantity", String.valueOf(quantity));
            map.add("total_amount", String.valueOf(totalAmount));
            map.add("tax_free_amount", "0");
            map.add("approval_url", approvalUrl);
            map.add("cancel_url", cancelUrl);
            map.add("fail_url", failUrl);
            return map;
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ApprovePaymentRequest {
        private String cid;
        private String tid; // kakaopay 발급
        private Long userId;
        private String pgToken;

        public MultiValueMap<String, String> toMultiValueMap(){
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("cid", cid);
            map.add("tid", tid);
            map.add("partner_order_id", tid);
            map.add("partner_user_id", userId.toString());
            map.add("pg_token", pgToken);
            return map;
        }
    }

    @Getter
    @Setter
    public static class KakaoPayApproveResponse {
        private String tid;
        private String partner_order_id;
        private String partner_user_id;
        private String payment_method_type;
        private Amount amount;
        private String approved_at;
        @Getter
        @Setter
        public static class Amount {
            private int total;
            private int vat;
        }
    }

}
