package com.dog.shop.web;

import com.dog.shop.domain.OrderItem;
import com.dog.shop.domain.User;
import com.dog.shop.domain.cart.Cart;
import com.dog.shop.domain.cart.CartItem;
import com.dog.shop.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Hex;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class TestController2 {

    private final CartItemRepository cartItemRepository;

    private static final String merchantKey = "EYzu8jGGMfqaDEp76gSckuvnaHHu+bC4opsSN6lHv3b2lurNYkVXrZ7Z1AoqQnXI3eLuaUFyoRNC6FkrzVjceg==";
    private static final String merchantID = "nicepay00m";
    //private static final String goodsName = "나이스페이";
    //private static final String price = "1004";
    // private static final String buyerName = "나이스";
    // private static final String buyerTel = "01000000000";
    // private static final String buyerEmail = "happy@day.co.kr";
    // private static final String moid = "mnoid1234567890";
    private static final String returnURL = "http://localhost:8080/nicepay3.0_utf-8/payResult_utf.jsp";
    @ResponseBody
    @PostMapping("/updateCartItemQuantity")
    public ResponseEntity<?> updateQuantity(@RequestParam Long itemId, @RequestParam int quantity) {
        CartItem cartItem = cartItemRepository.findById(itemId).orElseThrow(); // Or handle not found

        cartItem.setQuantity(quantity);
        cartItem.setSubTotal(cartItem.getUnitPrice() * quantity);

        cartItemRepository.save(cartItem);

        Cart cart = cartItem.getCart();
        double newTotalPrice = cart.calculateTotalPrice();

        Map<String, Object> response = new HashMap<>();
        response.put("newSubtotal", cartItem.getSubTotal());
        response.put("newTotalPrice", newTotalPrice);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/testTest")
    public String paymentTesting(
            @ModelAttribute("orderItemList") List<OrderItem> orderItemList,
            @ModelAttribute("productTotalPrice") Integer productTotalPrice,
            @ModelAttribute("feePrice") Integer feePrice,
            @ModelAttribute("moid") String moid,
            @ModelAttribute("user") User user,
            Model model) {

        String goodsName = orderItemList.get(0).getProduct().getProductName();
        if(orderItemList.size() > 1) {
            goodsName += " 외 " + (orderItemList.size() - 1) + "개";
        }

        model.addAttribute("merchantID", merchantID);
        model.addAttribute("goodsName", goodsName);
        int price = productTotalPrice + feePrice;
        model.addAttribute("price", price);
        model.addAttribute("buyerName", user.getName());
        model.addAttribute("buyerTel", user.getPhoneNumber());
        model.addAttribute("moid", moid);
        model.addAttribute("returnURL", returnURL);
        model.addAttribute("ediDate", getyyyyMMddHHmmss());
        model.addAttribute("signData", encrypt(getyyyyMMddHHmmss() + merchantID + price + merchantKey));
        return "test-test";
    }




    private synchronized String getyyyyMMddHHmmss() {
        SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");
        return yyyyMMddHHmmss.format(new Date());
    }

    private String encrypt(String strData) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.reset();
            md.update(strData.getBytes());
            byte[] raw = md.digest();
            return new String(Hex.encodeHex(raw));
        } catch (Exception e) {
            throw new RuntimeException("Encryption error", e);
        }
    }


    @GetMapping("/password")
    public String passwordPage() {
        return "password";
    }
}
