package com.dog.shop.utils;

import com.dog.shop.myenum.InquiryStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class InquiryStatusConverter implements AttributeConverter<InquiryStatus, String> {

    @Override
    public String convertToDatabaseColumn(InquiryStatus status) {
        if (status == null) {
            return null;
        }
        return status.getValue();
    }

    @Override
    public InquiryStatus convertToEntityAttribute(String value) {
        if (value == null) {
            return null;
        }
        return InquiryStatus.fromValue(value);
    }
}