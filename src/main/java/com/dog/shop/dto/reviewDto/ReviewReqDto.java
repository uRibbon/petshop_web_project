package com.dog.shop.dto.reviewDto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ReviewReqDto {
    // 입력화면
    private String title;
    private String content;
}
