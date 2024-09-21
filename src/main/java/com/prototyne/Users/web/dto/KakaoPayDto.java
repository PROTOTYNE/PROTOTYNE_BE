package com.prototyne.Users.web.dto;

import com.prototyne.domain.enums.TicketOption;
import lombok.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KakaoPayDto {

    @Getter
    @Setter
    @Data
    public static class KakaoPayReadyResponse {
        private String tid;
        private String next_redirect_app_url;
        private String next_redirect_mobile_url;
        private String next_redirect_pc_url;
        private String android_app_scheme;
        private String ios_app_scheme;
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


        public KakaoPayRequest(Long userId, TicketOption ticketOption) {
            this.partnerOrderId = UUID.randomUUID().toString();
            this.userId = userId;
            this.itemName = "[프로토타인] " + ticketOption.getTicketNumber() + " 티켓 구매";
            this.quantity = 1;
            this.totalAmount = ticketOption.getPrice();
            this.ticketOption = String.valueOf(ticketOption);
        }

        public Map<String, Object> toFormData(String cid, String approvalUrl, String cancelUrl, String failUrl) {
            Map<String, Object> map = new HashMap<>();
            map.put("cid", cid);  // 테스트용 CID
            map.put("partner_order_id", partnerOrderId);
            map.put("partner_user_id", userId.toString());
            map.put("item_name", itemName);
            map.put("quantity", String.valueOf(quantity));
            map.put("total_amount", String.valueOf(totalAmount));
            map.put("tax_free_amount", "0");
            map.put("approval_url", approvalUrl);
            map.put("cancel_url", cancelUrl);
            map.put("fail_url", failUrl);
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

        public Map<String, Object> toFormData() {
            Map<String, Object> map = new HashMap<>();
            {
                map.put("cid", cid);
                map.put("tid", tid);
                map.put("partner_order_id", tid);
                map.put("partner_user_id", userId.toString());
                map.put("pg_token", pgToken);
                return map;
            }
        }
    }

        @Getter
        @Setter
        @ToString
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
