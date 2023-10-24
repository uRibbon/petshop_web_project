package com.dog.shop.service;

import com.dog.shop.domain.CartItem;
import com.dog.shop.dto.CartItemReqDto;
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

        public CartItemResDto saveCartItem(CartItemReqDto cartItemReqDTO) {
                CartItem cartItem = modelMapper.map(cartItemReqDTO,CartItem.class); // entity 타입으로 변환 db와직접적연결
                CartItem savedCartItem = cartItemRepository.save(cartItem); // 그이후 레파지토리에서 엔티티객체를 넣어준다.
                return modelMapper.map(savedCartItem,CartItemResDto.class); //레파지토리.save도 스프링에서 제공해줌
        }


}