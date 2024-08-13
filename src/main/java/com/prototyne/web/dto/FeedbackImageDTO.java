package com.prototyne.web.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackImageDTO {
    private Long id;
    private String imageUrl;
    private Long feedbackId;  // 이 이미지가 속한 피드백의 ID
    private List<MultipartFile> feedbackImage;
}