package com.prototyne.Enterprise.converter;

import com.prototyne.Enterprise.web.dto.UserlistDTO;
import com.prototyne.domain.Investment;
import com.prototyne.domain.User;
import com.prototyne.domain.enums.InvestmentShipping;
import com.prototyne.domain.enums.InvestmentStatus;

public class UserlistConverter {

    public static UserlistDTO.UserListResponse toUserlistResponse(Investment investment, User user){

        String prizeStatus;
        if(investment.getStatus()==InvestmentStatus.신청)
        {
            prizeStatus="미당첨";
        }
        else
        {prizeStatus="당첨";}

        return UserlistDTO.UserListResponse.builder()
                .userName(user.getUsername())
                .event_start(investment.getCreatedAt())
                .prizeStatus(prizeStatus)
                .deliveryStatus(String.valueOf(InvestmentShipping.배송전))
                .reviewStatus("미작성")
                .addInfo("가족 구성원: "+user.getFamilyMember())
                .build();
    }
}
