package com.dog.shop.web.CartItem;

import com.dog.shop.domain.CartItem;
import com.dog.shop.dto.*;
import com.dog.shop.product.dto.ProductResDTO;
import com.dog.shop.product.service.ProductService;
import com.dog.shop.service.CartItemService;
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

    // 장바구니 목록 보여주기
    @GetMapping("/getList")
    public ModelAndView getList() {
        List<CartItemResDto> cartItems = cartItemService.getCartItems();
        return new ModelAndView("CartItem", "cartItems", cartItems);
    }
    // 상품의 정보를 가져오면서 장바구니 등록설정창 들어가기
    @GetMapping("/signup/{id}")
    public String showSignUpForm(@PathVariable Long id, Model model, CartItemReqDto cartItemReqDto) {
        ProductResDTO productResDTO = productService.getProductById(id);
        MultiFormDto multiFormDto = new MultiFormDto();
        multiFormDto.setCartItemReqDto(cartItemReqDto);
        multiFormDto.setProductResDTO(productResDTO);

        model.addAttribute("multiFormDto", multiFormDto);

        return "add-cartItem";
    } // 장바구니 등록
    @PostMapping("/addcartItem")
    public String addCartItem(@ModelAttribute("multiFormDto") MultiFormDto multiFormDto){
        CartItemReqDto cartItemReqDto = multiFormDto.getCartItemReqDto();
        ProductResDTO productResDTO = multiFormDto.getProductResDTO();
        CartReqDto cartReqDto = multiFormDto.getCartReqDto();

        // 여기에서 서비스 클래스를 호출하여 데이터를 저장
        cartItemService.saveCartItem(cartItemReqDto,productResDTO,cartReqDto);

        return "redirect:/CartItem/getList";
    }

// 장바구니 수정폼가기
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model){
        CartItemResDto cartItemResDto = cartItemService.getCartItemById(id);
    model.addAttribute("cartItem",cartItemResDto);
    return "update-cartItem";
}

// 장바구니 수정 하기
    @PostMapping("/update/{id}")
    public String updateCartItem(@PathVariable("id") long id, @Valid CartItemReqDto cartItem,
                             BindingResult result, Model model) { //id를써야해서 폼을씀
        if (result.hasErrors()) {
            System.out.println(">>> hasErros cartItem "  + cartItem);
            model.addAttribute("cartItem",cartItem);
            return "update-cartItem";
        }
//        cartItemService.updateCustomerForm(cartItem);
        cartItemService.updateCartItem(id,cartItem);
        return "redirect:/CartItem/getList";
    }

    // 장바구니 삭제
    @GetMapping("/delete/{id}")
    public String deleteCartItem(@PathVariable("id") long id) {
        cartItemService.deleteCartItem(id);
        return "redirect:/CartItem/getList";
    }


}