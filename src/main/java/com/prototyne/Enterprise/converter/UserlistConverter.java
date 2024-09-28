package com.prototyne.Enterprise.converter;

import com.prototyne.Enterprise.web.dto.UserlistDTO;
import com.prototyne.domain.Investment;
import com.prototyne.domain.User;
import com.prototyne.domain.enums.InvestmentShipping;
import com.prototyne.domain.enums.InvestmentStatus;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class UserlistConverter {

    public static UserlistDTO.UserListResponse toUserlistResponse(Investment investment, User user,String reviewStatus){

        String prizeStatus;
        Boolean deliveryStatus;
        if(Objects.equals(String.valueOf(investment.getStatus()), "신청"))
        {
            prizeStatus="미당첨";
        }
        else {prizeStatus="당첨";}

        if(Objects.equals(String.valueOf(investment.getShipping()), "배송후")){
            deliveryStatus=true;
        }
        else {deliveryStatus=false;}

        return UserlistDTO.UserListResponse.builder()
                .userId(user.getId())
                .userName(user.getUsername())
                .event_start(investment.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                .prizeStatus(prizeStatus)
                .deliveryStatus(deliveryStatus)
                .reviewStatus(reviewStatus)
                .birth(user.getBirth())
                .gender(user.getGender())
                .familyMember(user.getFamilyMember())
                .build();
    }
}
