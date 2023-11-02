package com.dog.shop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PaymentCancellation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cancelResultCode; // 취소 결과 코드
    private String cancelResultMsg;  // 취소 결과 메시지
    private int cancelAmt;           // 취소 금액
    private LocalDateTime cancelDateTime;  // 취소 날짜와 시간

    @ManyToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;
}