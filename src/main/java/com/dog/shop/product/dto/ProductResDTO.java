package com.dog.shop.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class ProductResDTO {

    private Long id;
    private String description;
    private int price;
    private String productName;
    private String salesStatus;
    private int stock;
    private String MainImageUrl;

}