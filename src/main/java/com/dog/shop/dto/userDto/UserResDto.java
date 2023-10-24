package com.dog.shop.dto.userDto;

import com.dog.shop.myenum.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserResDto {
    // 출력
    private Long id;
    private String email;
    private String password;
    private String name;
    private String address;
    private String phoneNumber;
    private LocalDate birthDate;
    private LocalDateTime createdDate;

    @Enumerated(EnumType.STRING)
    private Role role;
}
