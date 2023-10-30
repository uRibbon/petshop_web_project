package com.dog.shop.web;

import com.dog.shop.dto.reviewDto.ReviewReqDto;
import com.dog.shop.dto.reviewDto.ReviewResDto;
import com.dog.shop.product.service.ProductService;
import com.dog.shop.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final ProductService productService;

    // 리뷰 작성 페이지 불러오기
    @GetMapping("/writeReview/{orderItemId}")
    public String reviewWritePage(@PathVariable Long orderItemId, Model model) {
        Long userId = 2L; // 현재 로그인한 사용자의 ID를 가져와야 합니다.

        model.addAttribute("userId", userId);
        model.addAttribute("orderItemId", orderItemId);

        return "reviewWrite"; // 리뷰 작성 페이지로 이동
    }

    // 리뷰 쓰기
    /*@PostMapping("/writeReview/{productId}")
    public ResponseEntity<Boolean> writeReview(@PathVariable Long productId, ReviewReqDto reviewReqDto) {
        Long userId = 2L;
        Long productId1 = productId;
        boolean result = reviewService.writeReview(userId,productId1 ,reviewReqDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }*/
    @PostMapping("/writeReview/{orderItemId}")
    public String writeReview(@PathVariable Long orderItemId, ReviewReqDto reviewReqDto) {
        Long userId = 2L;
        reviewService.writeReview(userId, orderItemId, reviewReqDto);
        return "redirect:/";
    }

    //리뷰삭제
    @ResponseBody
    @PostMapping("/deleteReview/{reviewId}")
    public ResponseEntity<Boolean> deleteReview(@PathVariable Long reviewId) {
        boolean result = reviewService.deleteReview(reviewId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // 리뷰 수정
    @ResponseBody
    @PostMapping("/updateReview/{reviewId}")
    public Boolean updateReview(@PathVariable Long reviewId, ReviewReqDto reviewReqDto) {
        return reviewService.updateReview(reviewId, reviewReqDto);
    }

    // 회원별 리뷰 불러오기
    /*@ResponseBody
    @GetMapping("/myReview/{userId}")
    public ResponseEntity<List<ReviewResDto>> myReview(@PathVariable Long userId){
        List<ReviewResDto> result = reviewService.myReview(userId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }*/

    // 회원별 리뷰 가져오는거
    @GetMapping("/myReview/get/{userId}")
    public String myReview(@PathVariable Long userId, Model model){
        userId = 2L;
        List<ReviewResDto> result = reviewService.myReview(userId);
        model.addAttribute("result", result); // 이 부분을 추가합니다.
        return "reviewCheck";
    }


    // 제품별 리뷰 가져오기
    @ResponseBody
    @GetMapping("/myReview/{productId}")
    public ResponseEntity<List<ReviewResDto>> productReview(@PathVariable Long productId) {
        List<ReviewResDto> reviewResDtoList = reviewService.showReviewByProductId(productId);
        return new ResponseEntity<>(reviewResDtoList, HttpStatus.OK);
    }
}
