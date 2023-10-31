package com.dog.shop.product.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ProductReqForm {

    private Long id;
    private String description;
    private int price;
    private String productName;
    private String salesStatus;
    private int stock;
    private String MainImageUrl;
}
