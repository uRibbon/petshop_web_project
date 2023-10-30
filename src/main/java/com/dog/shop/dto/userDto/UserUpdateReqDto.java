package com.dog.shop.dto.userDto;

import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserUpdateReqDto {
    @Email
    private String email;

    private String name;
    private String address;
    private String detailAddress;
    private String phoneNumber;
    private LocalDate birthDate;
}
