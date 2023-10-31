package com.dog.shop.myenum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InquiryStatus {
    COMPLETED("COMPLETED", "답변완료"),
    INCOMPLETED("INCOMPLETED", "답변미완료");

    private final String key;
    private final String value;

    public static InquiryStatus fromValue(String value) {
        for (InquiryStatus status : InquiryStatus.values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid InquiryStatus value: " + value);
    }
}
