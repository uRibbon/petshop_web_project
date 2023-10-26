package com.dog.shop.dto;

import com.dog.shop.product.dto.ProductResDTO;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MultiFormDto {

    private CartItemReqDto cartItemReqDto;
    private ProductResDTO productResDTO;
    private CartResDto cartResDto;


}
