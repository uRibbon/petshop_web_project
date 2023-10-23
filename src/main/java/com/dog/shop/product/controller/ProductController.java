package com.dog.shop.product.controller;

import com.dog.shop.product.dto.ProductReqDTO;
import com.dog.shop.product.dto.ProductResDTO;
import com.dog.shop.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
    public String SignUpForm(ProductReqDTO product) {
        return "create-product";
    }

    @PostMapping("/create")
    public String createProduct(@Valid ProductReqDTO product, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "create-product";
        }

        productService.saveProduct(product);
        return "redirect:/products/list";
    }
}