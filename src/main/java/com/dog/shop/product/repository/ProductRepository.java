package com.dog.shop.product.repository;

import com.dog.shop.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findProductById(Long productId);
}
