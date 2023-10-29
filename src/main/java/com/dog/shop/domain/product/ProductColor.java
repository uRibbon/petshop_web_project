package com.dog.shop.domain.product;

import com.dog.shop.domain.Color;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table
public class ProductColor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "join_id") // @JoinColumn(name= 컬럼명)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "color_id")
    private Color color;
}
