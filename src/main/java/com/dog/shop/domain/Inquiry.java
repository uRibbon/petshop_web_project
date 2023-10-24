package com.dog.shop.domain;

import com.dog.shop.myenum.InquiryStatus;
import com.dog.shop.myenum.SalesStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table
public class Inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title; // 문의 제목
    private String content; // 문의 내용
//    private String status; // 문의 처리 상태 Enum으로 변경 예정

    @Enumerated(EnumType.STRING)
    private InquiryStatus inquiryStatus; // 문의 처리 상태

    private String response; // 관리자의 답변 내용
    private LocalDateTime responseDate; // 답변이 작성된 날짜와 시간

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
