package com.dog.shop.product.controller;

import com.dog.shop.domain.popularSearchedKeyword.resDTO.PopularSearchedKeywordResDTO;
import com.dog.shop.product.dto.ProductReqDTO;
import com.dog.shop.product.dto.ProductReqForm;
import com.dog.shop.product.dto.ProductResDTO;
import com.dog.shop.product.service.ProductService;
import com.dog.shop.service.popularKeyword.PopularKeywordService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final PopularKeywordService popularKeywordService;
    @GetMapping("/list")
    public ModelAndView listProducts(@RequestParam(defaultValue = "0") int page, Model model) {
        Pageable pageable = PageRequest.of(page, 6);
        Page<ProductResDTO> products = productService.findAllProducts(pageable);
        List<PopularSearchedKeywordResDTO> popularSearchedKeywordResDTOList = popularKeywordService.getResult();

        int totalPages = products.getTotalPages();
        int currentPage = pageable.getPageNumber() + 1;
        List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());

        model.addAttribute("currentPage", pageable.getPageNumber() + 1); // 페이지 번호는 0부터 시작하므로 1을 더해줍니다.
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("products", products);
        model.addAttribute("pageNumbers", pageNumbers);

        model.addAttribute("keywords", popularSearchedKeywordResDTOList);
        return new ModelAndView("product-list", "products", products);
    }
    @GetMapping("/signup")
    public String showSignUpForm(ProductReqDTO product) {
        return "create-product";
    }

    @ResponseBody
    @PostMapping("/create")
    public ResponseEntity<String> createProduct (
            @RequestParam("product") String productJson,
            @RequestParam(name = "mainImage", required = false) MultipartFile file) throws JsonProcessingException {

        // ObjectMapper를 사용하여 JSON 문자열을 DTO 객체로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        ProductReqDTO productReqDTO = objectMapper.readValue(productJson, ProductReqDTO.class);
        System.out.println(productReqDTO);

        if (file != null && !file.isEmpty()) {
            String imageUrl = productService.saveImage(file);
            productReqDTO.setMainImageUrl(imageUrl);
        }

        productService.saveProduct(productReqDTO);
        return ResponseEntity.ok("이미지 저장 성공");
    }

    @GetMapping("/{id}")
    public String productDetail(@PathVariable("id") Long id, Model model) {
        ProductResDTO productResDTO = productService.getProductById(id);
        model.addAttribute("product", productResDTO);
        return "product-detail-list";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model){
        ProductResDTO productResDTO = productService.getProductById(id);
        model.addAttribute("product",productResDTO);
        return "update-product";
    }



    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") long id) {
        productService.deleteProduct(id);
        return "redirect:/products/list";
    }
}