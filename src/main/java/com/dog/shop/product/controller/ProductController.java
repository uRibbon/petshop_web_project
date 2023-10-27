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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public String showSignUpForm(ProductReqDTO product) {
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

    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable("id") long id, @Valid ProductReqForm product,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("product", product);

            return "update-product";
        }

        productService.updateProduct(product);

        return "redirect:/products/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") long id) {
        productService.deleteProduct(id);
        return "redirect:/products/list";
    }
}