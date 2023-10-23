package com.dog.shop.product.controller;

import com.dog.shop.product.dto.ProductResDTO;
import com.dog.shop.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/list")
    public ModelAndView listProducts(Pageable pageable) {
        Page<ProductResDTO> products = productService.findAllProducts(pageable);
        return new ModelAndView("product-list", "products", products);
    }
}
