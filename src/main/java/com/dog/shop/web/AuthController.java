package com.dog.shop.web;

import com.dog.shop.dto.UserReqDto;
import com.dog.shop.dto.UserResDto;
import com.dog.shop.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("/check")
    public ResponseEntity<Boolean> memCheck(@RequestParam String email) {
        System.out.println("이메일 확인 : " + email);
        boolean result = authService.userCheck(email);
        System.out.println(result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<Boolean> saveUser(@RequestBody UserReqDto userReqDto) {
        boolean result = authService.signUser(userReqDto);

        System.out.println("컨트롤러" + userReqDto.getEmail());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping
    public List<UserResDto> getUser() {
        return authService.getUser();
    }

}
