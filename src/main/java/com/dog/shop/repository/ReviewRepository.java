package com.dog.shop.repository;

import com.dog.shop.domain.Review;
import com.dog.shop.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<User, Long> {
    /*List<Review> findByProductProductId(Long productId);*/
    /*List<Review> findByUserUserId(Long userId);*/

}
//package com.dog.shop.repository;
//
//import com.dog.shop.domain.User;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public interface ReviewRepository extends JpaRepository<User, Long> {
//    /*List<Review> findByProductProductId(Long productId);*/
//    /*List<Review> findByUserUserId(Long userId);*/
//
//}
