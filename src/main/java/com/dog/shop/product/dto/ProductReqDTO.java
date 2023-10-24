package com.dog.shop.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductReqDTO {

    private String description;
    private int price;
    private String productName;
    private String salesStatus;
    private int stock;

}
