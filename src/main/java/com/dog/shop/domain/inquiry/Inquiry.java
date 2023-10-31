package com.dog.shop.domain.inquiry;

import com.dog.shop.domain.Reply;
import com.dog.shop.domain.product.Product;
import com.dog.shop.domain.User;
import com.dog.shop.myenum.InquiryStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "inquiry")
public class Inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title; // 문의 제목
    private String content; // 문의 내용

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

    @OneToOne(mappedBy = "inquiry")
    private Reply reply;
}