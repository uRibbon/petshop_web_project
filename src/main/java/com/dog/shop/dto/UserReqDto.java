package com.dog.shop.dto;

import com.dog.shop.myenum.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    private String email;
    private String password;
    private String name;
    private String address;
    private String phoneNumber;
    private LocalDate birthDate;
    private char gender;

    @Enumerated(EnumType.STRING)
    private Role role;
}
