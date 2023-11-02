package com.dog.shop.domain;

import com.dog.shop.domain.time.BaseTimeEntity;
import com.dog.shop.myenum.payment.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table
public class Payment extends BaseTimeEntity { // 주문이 일어나고 그 다음에 결제가 일어남

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String paymentMethod; // 결제 방법: CARD로 고정
    // TODO 추후에 Enum으로 바꿀예정

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus; // 결제 상태
    private int amt; // 결제된 총 금액
    private String tid; // 거래번호

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @OneToMany(mappedBy = "payment")
    private List<PaymentCancellation> paymentCancellationList;

}
