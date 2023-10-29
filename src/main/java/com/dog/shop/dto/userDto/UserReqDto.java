package com.dog.shop.dto.userDto;

import com.dog.shop.myenum.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
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
    @Email
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

    // private char agree;
    // private char agreeSelect;
    // private Role role;
}