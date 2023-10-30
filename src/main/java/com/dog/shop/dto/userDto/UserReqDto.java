package com.dog.shop.dto.userDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserReqDto {
    //입력화면
    @NotBlank(message="이메일을 입력해주세요.")
    @Email(message = "올바른 이메일 주소를 입력해주세요.")
    private String email;

    private String password;
    private String name;
    private String address;
    private String detailAddress;
    private String phoneNumber;
    private LocalDate birthDate;
    private String chkTerms;
    private String chkPrivacy;
    private String chkMarketing;
}