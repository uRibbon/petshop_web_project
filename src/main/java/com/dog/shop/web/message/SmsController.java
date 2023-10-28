package com.dog.shop.web.message;

import com.dog.shop.dto.messageDto.MessageReqDTO;
import com.dog.shop.service.message.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sms")
public class SmsController {

   @Autowired
   private SmsService smsService;

   @PostMapping("/send")
   public ResponseEntity<String> sendSmsMessage(@RequestBody MessageReqDTO messageReqDTO) {
      smsService.sendMessageToPhone(messageReqDTO);
      return ResponseEntity.ok("Message sent successfully");
   }
}