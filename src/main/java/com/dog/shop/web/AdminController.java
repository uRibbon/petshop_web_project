package com.dog.shop.web;


import com.dog.shop.dto.inquiryDto.InquiryResDTO;
import com.dog.shop.product.dto.ProductReqDTO;
import com.dog.shop.product.dto.ProductReqForm;
import com.dog.shop.service.AdminService;
import com.dog.shop.service.inquiry.InquiryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final InquiryService inquiryService;

    // 상품등록
    @PostMapping("/create")
    public String createProduct(@Valid ProductReqDTO product, @RequestParam("mainImage") MultipartFile file , BindingResult result) {
        if (result.hasErrors()) {
            return "create-product";
        }
        // 이미지를 저장하고 URL을 반환하는 메서드를 productService에서 구현합니다.
        String imageUrl = adminService.saveImage(file);

        // productReqDTO에 이미지 커스텀마이징한 imageUrl 정보 삽입
        product.setMainImageUrl(imageUrl);

        adminService.saveProduct(product);
        return "redirect:/products/list";
    }

    // 상품 업로드
    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable("id") long id, @Valid ProductReqForm product,
                                BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("product", product);

            return "update-product";
        }

        adminService.updateProduct(product);

        return "redirect:/products/list";
    }

    // 상품 삭제
    @ResponseBody
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") long id) {
        adminService.deleteProduct(id);
        return "redirect:/products/list";
    }

    // QnA 전체리스트 가져오기
    @ResponseBody
    @GetMapping("/allQna")
    public List<InquiryResDTO> getInquiries() {
        return adminService.getInquiries();
    }
}
