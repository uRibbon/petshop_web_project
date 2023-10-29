package com.dog.shop.repository;

import com.dog.shop.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findProductById(Long productId);

    List<Review> findUserByUserId(long userId);
}
