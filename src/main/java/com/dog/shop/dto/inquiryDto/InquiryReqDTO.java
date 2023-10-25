package com.dog.shop.dto.inquiryDto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class InquiryReqDTO {

    private Long productId;

    private String title; // 문의 제목

    private String content; // 문의 내용

}
