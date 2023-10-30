package com.dog.shop.product.controller;

import com.dog.shop.product.dto.ProductReqDTO;
import com.dog.shop.product.dto.ProductReqForm;
import com.dog.shop.product.dto.ProductResDTO;
import com.dog.shop.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/list")
    public ModelAndView listProducts(Pageable pageable) {
        Page<ProductResDTO> products = productService.findAllProducts(pageable);
        return new ModelAndView("product-list", "products", products);
    }
    @GetMapping("/signup")
    public String showSignUpForm(ProductReqDTO product) {
        return "create-product";
    }

    @PostMapping("/create")
    public String createProduct(@Valid ProductReqDTO product,@RequestParam("mainImage") MultipartFile file , BindingResult result) {
        if (result.hasErrors()) {
            return "create-product";
        }
        // 이미지를 저장하고 URL을 반환하는 메서드를 productService에서 구현합니다.
        String imageUrl = productService.saveImage(file);

        // productReqDTO에 이미지 커스텀마이징한 imageUrl 정보 삽입
        product.setMainImageUrl(imageUrl);

        productService.saveProduct(product);
        return "redirect:/products/list";
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