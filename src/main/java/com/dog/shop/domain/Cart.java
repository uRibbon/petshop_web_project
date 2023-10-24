package com.dog.shop.domain;

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
    private Long id;

    @OneToMany(mappedBy = "cart")   //디비에안생김 mappedBy = "cart"라는것은 밑에 CartItem의 필드를 나타낸다.
    private List<CartItem> cartItems = new ArrayList<>();
      // List<타입명>
}
