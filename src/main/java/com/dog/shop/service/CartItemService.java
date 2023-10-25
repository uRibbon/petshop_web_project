package com.dog.shop.service;

import com.dog.shop.domain.Cart;
import com.dog.shop.domain.CartItem;
import com.dog.shop.domain.Product;
import com.dog.shop.dto.CartItemReqDto;
import com.dog.shop.dto.CartItemResDto;
import com.dog.shop.dto.CartReqDto;
import com.dog.shop.product.dto.ProductResDTO;
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

        public void saveCartItem(CartItemReqDto cartItemReqDto, ProductResDTO productResDTO, CartReqDto cartReqDto) {
                // ProductResDTO를 Product 엔티티로 변환
                Product product = new Product();
                product.setId(productResDTO.getId());
                product.setPrice(productResDTO.getPrice());
                // 필요한 경우 더 많은 필드를 복사

                Cart cart = new Cart();
                cart.setId(cartReqDto.getId());

                //CartItem 엔티티에 데이터 설정
                CartItem cartItem = new CartItem();
                cartItem.setQuantity(cartItemReqDto.getQuantity());
                cartItem.setSubTotal(cartItemReqDto.getSubTotal());
                cartItem.setUnitPrice(productResDTO.getPrice());
                cartItem.setCart(cart);
                cartItem.setProduct(product);
                // 이부분은 product에 id와 price가 담기지만 부트내부적으로 외래키값을 직접 매핑한다.
                // CartItem 엔티티 저장

                cartItemRepository.save(cartItem);

        }
}