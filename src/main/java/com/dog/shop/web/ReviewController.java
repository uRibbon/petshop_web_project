package com.dog.shop.web;

import com.dog.shop.dto.reviewDto.ReviewReqDto;
import com.dog.shop.dto.reviewDto.ReviewResDto;
import com.dog.shop.product.service.ProductService;
import com.dog.shop.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final ProductService productService;

    // 리뷰 쓰기
    @PostMapping("/writeReview/{productId}")
    public ResponseEntity<Boolean> writeReview(@PathVariable Long productId, ReviewReqDto reviewReqDto) {
        Long userId = 2L;
        Long productId1 = productId;
        boolean result = reviewService.writeReview(userId,productId1 ,reviewReqDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //리뷰삭제
    @PostMapping("/deleteReview/{reviewId}")
    public ResponseEntity<Boolean> deleteReview(@PathVariable Long reviewId) {
        boolean result = reviewService.deleteReview(reviewId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // 리뷰 수정
    @PostMapping("/updateReview/{reviewId}")
    public Boolean updateReview(@PathVariable Long reviewId, ReviewReqDto reviewReqDto) {
        return reviewService.updateReview(reviewId, reviewReqDto);
    }

    // 회원별 리뷰 불러오기
    @GetMapping("/myReview/{userId}")
    public ResponseEntity<List<ReviewResDto>> myReview(@PathVariable Long userId){
        List<ReviewResDto> result = reviewService.myReview(userId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // 제품별 리뷰 가져오기
    @GetMapping("/myReview/{productId}")
    public ResponseEntity<List<ReviewResDto>> productReview(@PathVariable Long productId) {
        List<ReviewResDto> reviewResDtoList = reviewService.showReviewByProductId(productId);
        return new ResponseEntity<>(reviewResDtoList, HttpStatus.OK);
    }
}
