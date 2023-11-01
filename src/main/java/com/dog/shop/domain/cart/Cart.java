package com.dog.shop.domain.cart;

import com.dog.shop.domain.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table
@Setter
public class Cart { // 장바구니

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@JsonProperty("cartId")
    private Long id;

    @OneToMany(mappedBy = "cart")   //디비에안생김 mappedBy = "cart"라는것은 밑에 CartItem의 필드를 나타낸다.
    private List<CartItem> cartItems = new ArrayList<>();
      // List<타입명>

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public String toString() {
        return "" + id;
    }

    public int getTotalItems() {
        return cartItems.size();
    }

    public int getTotalPrice() {
        return cartItems.stream()
                .mapToInt(item -> item.getSubTotal())
                .sum();
    }

    public double calculateTotalPrice() {
        double totalPrice = 0.0;

        // 모든 cartItems에 대해 합계를 계산합니다.
        for(CartItem item : cartItems) {
            totalPrice += item.getSubTotal();
        }

        return totalPrice;
    }
}
