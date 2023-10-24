package com.dog.shop.web.CartItem;

import com.dog.shop.dto.CartItemReqDto;
import com.dog.shop.dto.CartItemResDto;
import com.dog.shop.dto.ProductReqDTO;
import com.dog.shop.service.CartItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/CartItem")
@RequiredArgsConstructor
public class CartItemController {

    private final CartItemService cartItemService;

    @GetMapping("/getList")
    public ModelAndView getList() {
        List<CartItemResDto> cartItems = cartItemService.getCartItems();
        return new ModelAndView("CartItem", "cartItems", cartItems);
    }

    @GetMapping("/signup")
    public String showSignUpForm(CartItemReqDto cartItem, ProductReqDTO product) {
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
