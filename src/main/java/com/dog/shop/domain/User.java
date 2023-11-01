package com.dog.shop.domain;

import com.dog.shop.domain.cart.Cart;
import com.dog.shop.domain.time.BaseTimeEntity;
import com.dog.shop.myenum.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table
public class User extends BaseTimeEntity { // 사용자 가입일에 대한 내용은 BaseTimeEntity에서 자동으로 생성해줌
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("userId")
    private Long id;

    @Column(unique = true)
    private String email; // 이메일

    @JsonIgnore
    private String password; // 비밀번호

    private String name; // 이름

    private String address; // 주소
    private String detailAddress; // 상세주소

    @Column(unique = true)
    private String phoneNumber; // 전화번호

    private LocalDate birthDate; // 생년월일

    // private Character agree; // 약관동의(Y) -> 래퍼 클래스로 변경
    // private Character agreeSelect; // 약관동의(Y/N)

    private String chkTerms;
    private String chkPrivacy;
    private String chkMarketing;

    //private char gender; // 성별

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private Role role; // 권한

    private String oAuthProvider; // NAVER, KAKAO 존재

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();

    @OneToOne(mappedBy = "user" ,fetch = FetchType.LAZY)

    private Cart cart;
    //mappedBy 가 있으면 cart_id가 안생김 근데이미생겨서 JPA가 안건듬
}
