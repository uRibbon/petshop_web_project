package com.dog.shop.myenum.payment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentStatus {
    SUCCESS("SUCCESS", "성공"),
    FAIL("FAIL", "실패");

    private final String key;
    private final String value;
}