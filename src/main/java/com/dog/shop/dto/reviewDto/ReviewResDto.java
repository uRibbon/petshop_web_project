package com.dog.shop.dto.reviewDto;

import com.dog.shop.myenum.ReviewStatus;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ReviewResDto {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private String orderItemId;
    private ReviewStatus reviewStatus;
}