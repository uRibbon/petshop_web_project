package com.dog.shop.web.TrackingNumber;

import com.dog.shop.domain.Order;
import com.dog.shop.repository.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class TrackingNumberConroller {

    private final OrderRepository orderRepository;

    @GetMapping("/info")
    public String info(){
        return "info";
    }
    
    // 운송장 번호 넣기
    @ResponseBody
    @PostMapping("/tracking/{orderId}")
    public void makeTrackingNumber(@PathVariable Long orderId, @RequestParam String trackingNumber) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.setTrackingNumber(trackingNumber);
        orderRepository.save(order);
    }
    
    
}
