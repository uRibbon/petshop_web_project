package com.dog.shop.web;

import com.dog.shop.domain.cart.Cart;
import com.dog.shop.domain.cart.CartItem;
import com.dog.shop.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class TestController2 {

    private final CartItemRepository cartItemRepository;

    @PostMapping("/updateCartItemQuantity")
    public ResponseEntity<?> updateQuantity(@RequestParam Long itemId, @RequestParam int quantity) {
        CartItem cartItem = cartItemRepository.findById(itemId).orElseThrow(); // Or handle not found

        cartItem.setQuantity(quantity);
        cartItem.setSubTotal(cartItem.getUnitPrice() * quantity);

        cartItemRepository.save(cartItem);

        Cart cart = cartItem.getCart();
        double newTotalPrice = cart.calculateTotalPrice();

        Map<String, Object> response = new HashMap<>();
        response.put("newSubtotal", cartItem.getSubTotal());
        response.put("newTotalPrice", newTotalPrice);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/testTest")
    public String paymentTesting() {
        
        return "test-test";
    }
}
