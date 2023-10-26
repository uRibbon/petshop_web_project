package com.dog.shop.product.service;

import com.dog.shop.domain.Cart;
import com.dog.shop.domain.Product;
import com.dog.shop.dto.CartResDto;
import com.dog.shop.errorcode.ErrorCode;
import com.dog.shop.exception.CommonException;
import com.dog.shop.product.dto.ProductResDTO;
import com.dog.shop.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final ModelMapper modelMapper;
    private final CartRepository cartRepository;

    @Transactional(readOnly = true)
    public CartResDto getCartById(Long id) {
        Cart cartEntity = cartRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.NON_LOGIN, HttpStatus.NOT_FOUND));
        CartResDto cartResDto = modelMapper.map(cartEntity,CartResDto.class);
        return cartResDto;
    }
}
