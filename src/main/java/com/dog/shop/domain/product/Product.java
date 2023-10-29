package com.dog.shop.domain.product;

import com.dog.shop.domain.OrderItem;
import com.dog.shop.domain.cart.CartItem;
import com.dog.shop.myenum.SalesStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName; //상품명
    private int price; // 상품 가격
    private String mainImage; // 상품의 메인 이미지 경로 또는 URL
    private String subImage; // 상품의 서브 이미지
    private String description; // 상품에 대한 상세 설명
    private int stock; // 상품의 현재 재고
    private String MainImageUrl;
    @Enumerated(EnumType.STRING)
    private SalesStatus salesStatus; // 판매 상태

    @OneToMany(mappedBy = "product")
    private List<ProductColor> productColors = new ArrayList<>(); // 색상

    @OneToMany(mappedBy = "product")
    private List<ProductSize> productSizes = new ArrayList<>(); // 사이즈

    @OneToMany(mappedBy = "product")
    private List<CartItem> cartItems = new ArrayList<>();

    @Override
    public String toString() {
        return "" + id;
    }
    @OneToMany(mappedBy = "product")
    private List<OrderItem> orderItems = new ArrayList<>();

}
