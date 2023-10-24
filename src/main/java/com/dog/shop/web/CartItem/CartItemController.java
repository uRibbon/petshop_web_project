package com.dog.shop.web.CartItem;

import com.dog.shop.dto.*;
import com.dog.shop.service.CartItemService;
import com.dog.shop.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/CartItem")
@RequiredArgsConstructor
public class CartItemController {

    private final CartItemService cartItemService;
    private final ProductService productService;

    @GetMapping("/getList")
    public ModelAndView getList() {
        List<CartItemResDto> cartItems = cartItemService.getCartItems();
        return new ModelAndView("CartItem", "cartItems", cartItems);
    }

    @GetMapping("/signup/{id}")
    public String showSignUpForm(@PathVariable Long id, Model model,CartItemReqDto cartItemReqDto) {
        ProductResDTO productResDTO = productService.getProductById(id);
        MultiFormDto multiFormDto = new MultiFormDto();
        multiFormDto.setCartItemReqDto(cartItemReqDto);
        multiFormDto.setProductResDTO(productResDTO);

        model.addAttribute("multiFormDto",multiFormDto);

        return "add-cartItem";
    }

    @PostMapping("/addcartItem")
    public String addBook(@Valid CartItemReqDto cartItem, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-cartItem";
        }
        cartItemService.saveCartItem(cartItem);
        return "redirect:/CartItem/getList";
    }


}
