package com.dog.shop.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table
public class Payment extends BaseTimeEntity{ // 주문이 일어나고 그 다음에 결제가 일어남

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String paymentMethod; // 결제 방법
    // TODO 추후에 Enum으로 바꿀예정
    private String paymentStatus; // 결제 상태 (예: "Pending", "Completed", "Failed", "Refunded" 등).
    private int amt; // 결제된 총 금액
    private String tid; // 거래번호

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

}
