package com.prototyne.web.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackImageDTO {

    private Long id;
    private List<String> imageUrls;
    //private Long investmentId;
}