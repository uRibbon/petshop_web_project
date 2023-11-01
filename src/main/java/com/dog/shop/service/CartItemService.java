package com.dog.shop.service;


import com.dog.shop.domain.User;
import com.dog.shop.domain.cart.Cart;
import com.dog.shop.domain.cart.CartItem;
import com.dog.shop.domain.product.Product;
import com.dog.shop.dto.CartItemReqDto;
import com.dog.shop.dto.CartItemResDto;
import com.dog.shop.dto.CartReqDto;
import com.dog.shop.dto.CartResDto;
import com.dog.shop.errorcode.ErrorCode;
import com.dog.shop.exception.CommonException;
import com.dog.shop.product.dto.ProductResDTO;
import com.dog.shop.product.repository.ProductRepository;
import com.dog.shop.repository.CartItemRepository;
import com.dog.shop.repository.CartRepository;
import com.dog.shop.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional
public class CartItemService {
        private final CartItemRepository cartItemRepository;
        private final ModelMapper modelMapper;
        private final CartRepository cartRepository;
        private final ProductRepository productRepository;
        private final UserRepository userRepository;

        @Transactional(readOnly = true)
        public List<CartItemResDto> getCartItems() {
                List<CartItem> cartItemList = cartItemRepository.findAll();
                List<CartItemResDto> cartItemResDtoList = cartItemList.stream()
                        .map(cartItem -> modelMapper.map(cartItem,CartItemResDto.class))
                        .collect(toList());    //Entity를 Res로 그리고 다시 리스트로
                return cartItemResDtoList;
        }

        // 유저에서 가져오기

        // TODO 수정중
        @Transactional
        public List<CartItemResDto> getUserCartItems(Long userId) {
                // 처음 계정 생성시 Cart(장바구니가 없을시에 User Entity 를먼저 가져오는로직)
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
                // 처음 계정 생성시 Cart(장바구니가 없을때) 장바구니를 눌렀을때 장바구니를 새로 생성하고 장바구니로 들어가기
                Cart cart = cartRepository.findByUserId(userId)
                        .orElseGet(() -> {
                                Cart newCart = new Cart();
                                newCart.setUser(user);
                                return cartRepository.save(newCart);
                        });

                List<CartItem> cartItemList = cartItemRepository.findByCartId(cart.getId());
                List<CartItemResDto> cartItemResDtoList = cartItemList.stream()
                        .map(cartItem -> modelMapper.map(cartItem,CartItemResDto.class))
                        .collect(toList());    //Entity를 Res로 그리고 다시 리스트로
                return cartItemResDtoList;
        }

        public void saveCartItem(CartItemReqDto cartItemReqDto, ProductResDTO productResDTO, CartResDto cartResDto) {
                // 데이터베이스에서 실제 Product 엔티티 조회
                Product product = productRepository.findById(productResDTO.getId())
                        .orElseThrow(() -> new RuntimeException("Product not found"));

                // Cart 조회 (존재하지 않으면 새로 생성)
                Cart cart = cartRepository.findById(cartResDto.getId()).orElseGet(Cart::new);

                // CartItem 엔티티에 데이터 설정
                CartItem cartItem = new CartItem();
                cartItem.setQuantity(cartItemReqDto.getQuantity());
                cartItem.setSubTotal(cartItemReqDto.getSubTotal());
                cartItem.setUnitPrice(productResDTO.getPrice());
                cartItem.setCart(cart);
                cartItem.setProduct(product); // 조회된 Product 엔티티 설정

                // CartItem 엔티티 저장
                cartItemRepository.save(cartItem);
        }
        /*public void saveCartItem(CartItemReqDto cartItemReqDto, ProductResDTO productResDTO, CartResDto cartResDto) {
                // ProductResDTO를 Product 엔티티로 변환
                Product product = new Product();
                product.setId(productResDTO.getId());
                product.setPrice(productResDTO.getPrice());
                // 필요한 경우 더 많은 필드를 복사

                Cart cart = new Cart();
                cart.setId(cartResDto.getId());

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

        }*/

        @Transactional(readOnly = true)
        public CartItemResDto getCartItemById(Long id) {
                CartItem cartItemEntity = cartItemRepository.findById(id) // findById메소드자체리턴값이 엔티티임
                        .orElseThrow(() -> new CommonException(ErrorCode.NON_LOGIN , HttpStatus.NOT_FOUND));
                CartItemResDto cartItemResDto = modelMapper.map(cartItemEntity,CartItemResDto.class);
                return cartItemResDto;                  //Entity를 Res로
        }

        @Transactional
        public CartItemResDto updateCartItem(Long id, CartItemReqDto cartItemReqDto) {
                CartItem existCartItem = cartItemRepository.findById(id)
                        .orElseThrow(() ->
                                new CommonException(ErrorCode.NON_LOGIN,HttpStatus.NOT_FOUND));
                //Dirty Checking 변경감지를 하므로 setter method만 호출해도 update query가 실행이 된다.
                existCartItem.setQuantity(cartItemReqDto.getQuantity());
                existCartItem.setSubTotal(cartItemReqDto.getSubTotal());
                existCartItem.setUnitPrice(cartItemReqDto.getUnitPrice());
                return modelMapper.map(existCartItem, CartItemResDto.class);
        }

        public void deleteCartItem(Long id) {
                CartItem cartItem = cartItemRepository.findById(id)
                        .orElseThrow(() ->
                                new CommonException(ErrorCode.NON_LOGIN, HttpStatus.NOT_FOUND));
                cartItemRepository.delete(cartItem);
        }
}