package com.dog.shop.product.service;


import com.dog.shop.domain.cart.Cart;
import com.dog.shop.dto.CartResDto;
import com.dog.shop.errorcode.ErrorCode;
import com.dog.shop.exception.CommonException;
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
        try {
            Cart cartEntity = cartRepository.findById(id)
                    .orElseThrow(() -> new CommonException(ErrorCode.NON_LOGIN, HttpStatus.NOT_FOUND));
            CartResDto cartResDto = modelMapper.map(cartEntity, CartResDto.class);
            return cartResDto;
        } catch (CommonException e) {
            throw e; // 예외를 다시 던져서 컨트롤러에서도 예외를 처리할 수 있도록 함
        } catch (Exception e) {
            throw new CommonException(ErrorCode.NON_LOGIN, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
