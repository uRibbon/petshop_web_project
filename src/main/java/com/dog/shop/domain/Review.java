package com.dog.shop.domain;

import com.dog.shop.myenum.ReviewStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table
public class Review extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title; // 리뷰의 제목
    private String content; // 리뷰 내용

    @Enumerated(EnumType.STRING)
    private ReviewStatus reviewStatus; // 관리자의 고객 리뷰에 대한 응답

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "order_item_id")
    private OrderItem orderItem;
}
