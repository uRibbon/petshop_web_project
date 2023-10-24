package com.dog.shop.dto.inquiryDto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class InquiryReqDTO {

    private String email; // 문의하는 고객의 이메일

    private String productName; // 문의할 상품의 이름

    private String title; // 문의 제목

    private String content; // 문의 내용

}
