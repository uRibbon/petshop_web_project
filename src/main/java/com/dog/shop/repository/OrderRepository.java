package com.dog.shop.repository;

import com.dog.shop.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderItem, Long> {

}
