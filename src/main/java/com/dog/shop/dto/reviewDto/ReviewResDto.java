package com.dog.shop.dto.reviewDto;

import com.dog.shop.myenum.ReviewStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ReviewResDto {
    private Long id;
    private String title;
    private String content;
    private ReviewStatus reviewStatus;
    // private String email;
}