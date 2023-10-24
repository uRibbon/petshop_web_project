package com.dog.shop.domain.inquiry.reqDTO;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class InquiryReqDTO {

    private Long id;

    private String email;

    private Long productName;

    private String title;

    private String content;
}
