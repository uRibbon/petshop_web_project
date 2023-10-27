package com.dog.shop.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductReqDTO {

    private Long id;
    private String description;
    private int price;
    private String productName;
    private String salesStatus;
    private int stock;
    private MultipartFile mainImage;
    private String mainImageUrl;

}
