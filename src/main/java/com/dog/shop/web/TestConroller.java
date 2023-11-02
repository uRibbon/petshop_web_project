package com.dog.shop.web;

import com.dog.shop.domain.Order;
import com.dog.shop.repository.order.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestConroller {

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/api/register")
    @ResponseBody
    public String test() {
        return "test";
    }

    @GetMapping("/customLogin")
    public String index() {
        return "redirect:/index";
    }

    @GetMapping("/refund/order/{orderId}")
    public String refundOrder(@PathVariable Long orderId) {
        // TODO 에러 나중에 작성
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.setStatus("requireRefund");
        orderRepository.save(order);
        return "/order/check";  // orderPage는 리다이렉트 대상 URL입니다.
    }



}
