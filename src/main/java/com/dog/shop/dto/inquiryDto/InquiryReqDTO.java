package com.dog.shop.dto.inquiryDto;

import com.dog.shop.domain.Reply;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class InquiryReqDTO {

    private Long id;

    private Long productId;

    private String title; // 문의 제목

    private String content; // 문의 내용

    private Reply reply;
}
