package com.dog.shop.domain.popularSearchedKeyword.resDTO;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class PopularSearchedKeywordResDTO {

    private Long id;

    private String productName;

    private Integer keywordCount;
}
