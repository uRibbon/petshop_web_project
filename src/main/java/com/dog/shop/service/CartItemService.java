package com.dog.shop.service;

import com.dog.shop.domain.CartItem;
import com.dog.shop.dto.CartItemResDto;
import com.dog.shop.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional
public class CartItemService {
        private final CartItemRepository cartItemRepository;
        private final ModelMapper modelMapper;

        @Transactional(readOnly = true)
        public List<CartItemResDto> getCartItems() {
                List<CartItem> cartItemList = cartItemRepository.findAll();
                List<CartItemResDto> cartItemResDtoList = cartItemList.stream()
                        .map(cartItem -> modelMapper.map(cartItem,CartItemResDto.class))
                        .collect(toList());    //Entity를 Res로 그리고 다시 리스트로
                return cartItemResDtoList;
        }


}
