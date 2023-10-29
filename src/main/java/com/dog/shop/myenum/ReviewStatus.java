package com.dog.shop.myenum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReviewStatus {
    답변완료("COMPLETE", "답변완료"),
    답변대기("NOTYET", "답변대기");

    private final String key;
    private final String value;
}
