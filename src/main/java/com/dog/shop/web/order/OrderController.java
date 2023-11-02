package com.dog.shop.web.order;

import com.dog.shop.domain.Order;
import com.dog.shop.domain.OrderItem;
import com.dog.shop.domain.User;
import com.dog.shop.domain.cart.Cart;
import com.dog.shop.domain.cart.CartItem;
import com.dog.shop.repository.CartItemRepository;
import com.dog.shop.repository.CartRepository;
import com.dog.shop.repository.UserRepository;
import com.dog.shop.repository.order.OrderItemRepository;
import com.dog.shop.repository.order.OrderRepository;
import com.dog.shop.service.CartItemService;
import com.dog.shop.utils.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class OrderController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;



    @GetMapping("/order/check")
    public String orderCheckPage(Model model, HttpServletRequest request) { // 주문 조회 페이지
        String token = getJwtTokenFromCookies(request); // 쿠키에서 jwtToken 가져오기
        List<Order> orderList = null;
        User user = null;
        if (token != null) {
            String email = jwtUtil.getEmailFromToken(token); // 토큰에서 이메일 가져오기
            if (email != null) {
                // TODO 에러처리 필요
                user = userRepository.findByEmail(email).orElseThrow(); // 이메일을 통해 userId 가져오기
                // 이후 로직 처리...
                Long userId = user.getId();
                orderList = orderRepository.findByUserId(userId)
                        .stream()
                        .filter(order -> "completed".equals(order.getStatus()))
                        .collect(Collectors.toList());
            }
        }

        model.addAttribute("orderList", orderList);
        return "orderCheck";
    }

    @GetMapping("/order/{orderId}")
    public String orderDetails(@PathVariable Long orderId, Model model) {
        // TODO 에러처리 필요
        Order order = orderRepository.findById(orderId).orElseThrow();
        List<OrderItem> orderItems = order.getOrderItems();
        model.addAttribute("order", order);
        model.addAttribute("orderItems", orderItems);
        return "orderDetails";
    }


    @PostMapping("/preparePayment")
    public String preparePayment(@RequestParam String selectedItems, @RequestParam int feePrice, @RequestParam int totalPrice, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        // ... 기존 코드 ...
        String token = getJwtTokenFromCookies(request); // 쿠키에서 jwtToken 가져오기
        List<CartItem> cartItemList = null;
        User user = null;

        List<Long> selectedItemsList = Arrays.stream(selectedItems.split(","))
                .map(Long::valueOf)
                .collect(Collectors.toList());

        if (token != null) {
            String email = jwtUtil.getEmailFromToken(token); // 토큰에서 이메일 가져오기
            if (email != null) {
                // TODO 에러처리 필요
                user = userRepository.findByEmail(email).orElseThrow(); // 이메일을 통해 userId 가져오기
                // 이후 로직 처리...
                Long userId = user.getId();


                Cart cart = cartRepository.findByUserId(userId).orElseThrow();
                cartItemList = cartItemRepository.findAllById(selectedItemsList);
            }
        }
        if (cartItemList != null && !cartItemList.isEmpty()) {

            // goodsName과 price 계산
            String goodsName = cartItemList.get(0).getProduct().getProductName();
            if (cartItemList.size() > 1) {
                goodsName += " 외 " + (cartItemList.size() - 1) + "개";
            }
            // int price = cartItemList.stream().mapToInt(CartItem::getSubTotal).sum(); // + 배달비
            // int price = totalPrice; // + 배달비

            // 1. 복사 (Copy)
            Order order = new Order();
            order.setUser(user);
            order.setStatus("Pending");  // 초기 상태 설정
            order.setTotalPrice(totalPrice);
            orderRepository.save(order);

            for (CartItem cartItem : cartItemList) {
                OrderItem orderItem = new OrderItem();
                orderItem.setProduct(cartItem.getProduct());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setSubTotal(cartItem.getSubTotal());
                orderItem.setUnitPrice(cartItem.getUnitPrice());
                orderItem.setOrder(order);
                orderItemRepository.save(orderItem);
            }

            // 2. 삭제 (Delete)
            // 즉, cartItemList에 포함되지 않은 다른 CartItem 객체들은 그대로 데이터베이스에 남아 있게 됩니다.
            cartItemRepository.deleteAll(cartItemList);



            redirectAttributes.addFlashAttribute("goodsName", goodsName);
            redirectAttributes.addFlashAttribute("price", totalPrice);
            redirectAttributes.addFlashAttribute("feePrice", feePrice);
            redirectAttributes.addFlashAttribute("moid", order.getId()); // moid는 주문번호를 넣어줌!
            redirectAttributes.addFlashAttribute("user", user);
        }

        return "redirect:/testTest"; // payment.html로 리다이렉트
    }







    private String getJwtTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("JWT-TOKEN".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }


}
