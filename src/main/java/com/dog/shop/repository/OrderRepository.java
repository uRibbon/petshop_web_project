//package com.dog.shop.repository;
//
//import com.dog.shop.domain.Order;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.time.LocalDate;
//import java.util.List;
//
//public interface OrderRepository extends JpaRepository<Order, Long> {
//    List<Order> findByOrderDate(LocalDate orderDate);
//
//    List<Order> findByCartCartId(long cartId);
//
//    List<Order> findByUserUserId(long userId);
//}
