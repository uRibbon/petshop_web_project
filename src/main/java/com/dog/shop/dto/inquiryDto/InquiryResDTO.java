package com.dog.shop.dto.inquiryDto;

import com.dog.shop.myenum.InquiryStatus;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class InquiryResDTO {

    private String email;

    private String productName;

    private String title;

    private String content;

    private InquiryStatus inquiryStatus;

    private String response;

    private LocalDateTime responseDate;
}

