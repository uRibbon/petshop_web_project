package com.dog.shop.web;


import com.dog.shop.product.dto.ProductReqForm;
import com.dog.shop.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
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
}
