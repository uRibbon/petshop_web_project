package com.dog.shop.myenum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SalesStatus {
    SALE("SALE", "판매중"),
    SOLDOUT("SOLDOUT", "품절"),
    RESERVATION("RESERVATION", "예약"),
    STOP("STOP", "중지");

    private final String key;
    private final String value;
}
