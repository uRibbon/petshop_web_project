package com.dog.shop.domain.inquiry.resDTO;

import com.dog.shop.myenum.InquiryStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class InquiryResDTO {

//    private Long id = 2L;

    private String email;

    private Long productName;

    private String title;

    private String content;

    @Enumerated(EnumType.STRING)
    private InquiryStatus inquiryStatus;

    private String response;

    private LocalDateTime responseDate;

}
