package com.dog.shop.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.dog.shop.domain.OrderItem;
import com.dog.shop.domain.Review;
import com.dog.shop.domain.User;
import com.dog.shop.dto.reviewDto.ReviewReqDto;
import com.dog.shop.dto.reviewDto.ReviewResDto;
import com.dog.shop.exception.MemberNotFoundException;
import com.dog.shop.myenum.ReviewStatus;
import com.dog.shop.product.repository.ProductRepository;
import com.dog.shop.repository.ReviewRepository;
import com.dog.shop.repository.UserRepository;
import com.dog.shop.repository.order.OrderItemRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    private final OrderItemRepository orderItemRepository;

    private final ModelMapper modelMapper;

    // 리뷰 작성하기                  //ReviewController에서 보내는 orderItemId,userId 순서가 반대여서 값이 반대로들어가서 수정
    public boolean writeReview(Long orderItemId,Long userId, ReviewReqDto reviewReqDto) {
        User userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new MemberNotFoundException("user not found"));

        OrderItem itemEntity = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new MemberNotFoundException("orderItem not found"));

        Review review = new Review();
        review.setTitle(reviewReqDto.getTitle());
        review.setContent(reviewReqDto.getContent());
        review.setReviewStatus(ReviewStatus.답변대기);
        review.setUser(userEntity);
        review.setOrderItem(itemEntity);

        /*
         * if (reviewReqDto.getTitle() != null) {
         * review.setTitle(reviewReqDto.getTitle());
         * }
         */
        reviewRepository.save(review);
        return true;
    }

    // 리뷰삭제
    public boolean deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
        return true;
    }

    // 리뷰수정
    public boolean updateReview(Long reviewId, ReviewReqDto reviewReqDto) {
        Review reviewEntity = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new MemberNotFoundException("review not found"));
        reviewEntity.setTitle(reviewReqDto.getTitle());
        reviewEntity.setContent(reviewReqDto.getContent());
        reviewEntity.setReviewStatus(ReviewStatus.답변대기);
        reviewRepository.save(reviewEntity);
        return true;
    }

    // 회원별 리뷰 리스트 불러오기
    public List<ReviewResDto> myReview(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new MemberNotFoundException("user not found"));
        List<Review> reviewList = reviewRepository.findUserByUserId(userId);
        List<ReviewResDto> reviewResDtoList = new ArrayList<>();

        for (Review review : reviewList) {
            ReviewResDto reviewResDto = modelMapper.map(review, ReviewResDto.class);
            reviewResDtoList.add(reviewResDto);
        }
        return reviewResDtoList;
    }

    // 제품별 리뷰 가져오기
    public List<ReviewResDto> showReviewByProductId(Long productId) {
        List<Review> productList = reviewRepository.findProductById(productId);
        List<Review> reviewList = reviewRepository.findProductById(productId);
        List<ReviewResDto> reviewResDtoList = reviewList.stream()
                .map(review -> modelMapper.map(review, ReviewResDto.class))
                .collect(Collectors.toList());

        return reviewResDtoList;
    }

    // 리뷰 리스트 들고오기
    public List<ReviewResDto> showAllReview() {
        List<Review> reviewList = reviewRepository.findAll();

        List<ReviewResDto> reviewResDtoList = reviewList.stream()
                .map(review -> modelMapper.map(review, ReviewResDto.class))
                .collect(Collectors.toList());

        return reviewResDtoList;
    }

}
