package com.prototyne.Users.web.dto;

import com.prototyne.domain.enums.TicketOption;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
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
        private String orderId; // 결제 건 고유 번호
        private Long userId;
        private String itemName;
        private int quantity;
        private int totalAmount;
        private String ticketOption;


        public KakaoPayRequest(Long userId, TicketOption ticketOption){
            this.orderId = UUID.randomUUID().toString();
            this.userId = userId;
            this.itemName = "[프로토타인] "+ticketOption.getTicketNumber() + " 티켓 구매";
            this.quantity = 1;
            this.totalAmount = ticketOption.getPrice();
            this.ticketOption = String.valueOf(ticketOption);
        }

        public MultiValueMap<String, String> toMultiValueMap(String cid, String approvalUrl, String cancelUrl, String failUrl) {
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("cid", cid);  // 테스트용 CID
            map.add("partner_order_id", orderId);
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
}
