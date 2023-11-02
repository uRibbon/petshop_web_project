package com.dog.shop.web;

import com.dog.shop.domain.User;
import com.dog.shop.dto.reviewDto.ReviewReqDto;
import com.dog.shop.dto.reviewDto.ReviewResDto;
import com.dog.shop.help.JwtHelper;
import com.dog.shop.service.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final JwtHelper jwtHelper;
    // 리뷰 쓰기
    /*@PostMapping("/writeReview/{orderItemId}")
    public ResponseEntity<Boolean> writeReview(@PathVariable Long orderItemId, ReviewReqDto reviewReqDto) {
        Long userId = 2L;
        boolean result = reviewService.writeReview(orderItemId, userId, reviewReqDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }*/


    @GetMapping("/writeReview/{orderItemId}")
    public String writeReviewGet(@PathVariable Long orderItemId, ReviewReqDto reviewReqDto, HttpServletRequest request) {
        String token = jwtHelper.extractTokenFromCookies(request);
        Optional<User> userOpt = jwtHelper.extractUserFromToken(token);
        Long userId = userOpt.get().getId();
        return "reviewWrite";
    }

    @PostMapping("/writeReview/{orderItemId}")
    public String writeReview(@PathVariable Long orderItemId, ReviewReqDto reviewReqDto, HttpServletRequest request) {
        String token = jwtHelper.extractTokenFromCookies(request);
        Optional<User> userOpt = jwtHelper.extractUserFromToken(token);
        Long userId = userOpt.get().getId();
        boolean result = reviewService.writeReview(orderItemId, userId, reviewReqDto);
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
    @ResponseBody
    @GetMapping("/myReview/{userId}")
    public ResponseEntity<List<ReviewResDto>> myReview(@PathVariable Long userId){
        List<ReviewResDto> result = reviewService.myReview(userId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // 제품별 리뷰 가져오기
    @ResponseBody
    @GetMapping("/productReview/{orderItemId}")
    public ResponseEntity<List<ReviewResDto>> productReview(@PathVariable Long orderItemId) {
        List<ReviewResDto> reviewResDtoList = reviewService.showReviewByProductId(orderItemId);
        return new ResponseEntity<>(reviewResDtoList, HttpStatus.OK);
    }

    // 회원별 리뷰 가져오는거
    @GetMapping("/myReview/get") // userId를 밑에서 토큰으로 받을거라 매핑에서삭제
    public String myReview(Model model,HttpServletRequest request,
                           HttpServletResponse response){
        // 전단계 myPage에서 임시로 2를보내줬는데 해제하고 토큰으로 userId 정보받기
        String token = jwtHelper.extractTokenFromCookies(request);
        Optional<User> userOpt = jwtHelper.extractUserFromToken(token);
        Long userId = userOpt.get().getId();

        List<ReviewResDto> result = reviewService.myReview(userId);
        model.addAttribute("result", result); // 이 부분을 추가합니다.
        return "reviewCheck";
    }

    // 리뷰 가져오기
    @ResponseBody
    @GetMapping("/reviewList")
    public List<ReviewResDto> getReview() {
        return reviewService.showAllReview();
    }
}
