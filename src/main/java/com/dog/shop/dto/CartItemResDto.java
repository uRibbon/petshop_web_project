package com.dog.shop.dto;


import com.dog.shop.domain.cart.Cart;
import com.dog.shop.domain.product.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CartItemResDto {

    private Long id;
    private int quantity; // 장바구니에 추가된 제품의 수량
    private int unitPrice; // 제품 단위의 가격
    private int subTotal; // (unitPrice * quantity)로 계산된 합계 금액
    private Product product; // Product 정보를 담는 DTO 클래스
    private Cart cart;

}