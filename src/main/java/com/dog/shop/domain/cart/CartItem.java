package com.dog.shop.domain.cart;

import com.dog.shop.domain.product.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table
@Setter
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity; // 장바구니에 추가된 제품의 수량
    private int unitPrice; // 제품 단위의 가격
    private int subTotal; // (unitPrice * quantity)로 계산된 합계 금액

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "cart_id") // 외래키 이름명명
    private Cart cart;

}
