package com.dog.shop.web;


import com.dog.shop.dto.inquiryDto.InquiryResDTO;
import com.dog.shop.dto.replyDto.ReplyReqDto;
import com.dog.shop.dto.replyDto.ReplyResDto;
import com.dog.shop.product.dto.ProductReqDTO;
import com.dog.shop.product.dto.ProductReqForm;
import com.dog.shop.product.dto.ProductResDTO;
import com.dog.shop.service.AdminService;
import com.dog.shop.service.popularKeyword.PopularKeywordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final PopularKeywordService popularKeywordService;
    private final ModelMapper modelMapper;

    // 상품등록
    @PostMapping("/create")
    public ResponseEntity<Boolean> createProduct(@Valid ProductReqDTO product, @RequestParam("mainImage") MultipartFile file, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(false); // 유효성 검사 오류인 경우 false를 반환합니다.
        }

        String imageUrl = adminService.saveImage(file);
        product.setMainImageUrl(imageUrl);
        ProductResDTO savedProduct = adminService.saveProduct(product);

        if (savedProduct != null) {
            return ResponseEntity.ok(true); // 상품 등록이 성공한 경우 true를 반환합니다.
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false); // 서버 오류인 경우 false를 반환합니다.
        }
    }


    // 상품 업데이트
    @PostMapping("/update/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable("id") long id, @Valid ProductReqForm product, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("product", product);
            return ResponseEntity.badRequest().body("Validation Error: Invalid data provided.");
        }

        adminService.updateProduct(product);
        return ResponseEntity.ok("Product updated successfully!");
    }

    // 상품 삭제
    @PostMapping("/deleteProduct/{id}")
    public ResponseEntity<Boolean> deleteProduct(@PathVariable Long id) {
        boolean isDeleted = adminService.deleteProduct(id);
        return new ResponseEntity<>(isDeleted, HttpStatus.OK);
    }

    // 상품 리스트 가져오기
    @ResponseBody
    @GetMapping("/list")
    public List<ProductResDTO> listProducts() {
        return adminService.findAllProducts();
    }

    // QnA 전체리스트 가져오기
    @ResponseBody
    @GetMapping("/allQna")
    public List<InquiryResDTO> getInquiries() {
        return adminService.getInquiries();
    }

    //QnA댓글 달기
    @PostMapping("/qnaReply/{inquiryId}")
    public ResponseEntity<Boolean> qnaReply(@PathVariable Long inquiryId, @RequestBody ReplyReqDto replyReqDto) {
        boolean isReplySuccessful = adminService.QnaReply(inquiryId, replyReqDto);
        return new ResponseEntity<>(isReplySuccessful, HttpStatus.OK);
    }

    // QnA 리스트 가져오기
    @GetMapping
    public ResponseEntity<List<ReplyResDto>> getReplyList() {
        List<ReplyResDto> replyList = adminService.getReplyList();
        return new ResponseEntity<>(replyList, HttpStatus.OK);
    }

    @PostMapping("/deleteReply/{replyId}")
    public boolean replyDelete(@PathVariable Long replyId) {
        adminService.deleteReply(replyId);
        return true;
    }
}
