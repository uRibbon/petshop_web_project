package com.dog.shop.domain;

import com.dog.shop.domain.product.ProductSize;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table
public class Size {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String size; // 상품 사이즈

    @OneToMany(mappedBy = "size")
    private List<ProductSize> productSizes = new ArrayList<>();
}
