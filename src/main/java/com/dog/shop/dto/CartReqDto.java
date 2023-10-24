package com.dog.shop.dto;

import com.dog.shop.domain.CartItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CartReqDto {

    private Long id;
    private List<CartItem> cartItems;
}
