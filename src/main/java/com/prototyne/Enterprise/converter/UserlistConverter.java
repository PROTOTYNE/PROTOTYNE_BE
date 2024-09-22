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
        String deliveryStatus;
        if(Objects.equals(String.valueOf(investment.getStatus()), "신청"))
        {
            prizeStatus="미당첨";
        }
        else {prizeStatus="당첨";}

        return UserlistDTO.UserListResponse.builder()
                .userName(user.getUsername())
                .event_start(investment.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                .prizeStatus(prizeStatus)
                .deliveryStatus(String.valueOf(investment.getShipping()))
                .reviewStatus(reviewStatus)
                .birth("생년월일: "+user.getBirth())
                .gender("성별: "+user.getGender())
                .familyMember("가족 구성원: "+user.getFamilyMember())
                .build();
    }
}
