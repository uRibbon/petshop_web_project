package com.dog.shop.domain.crawling;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "popular_searched_keyword")
public class PopularSearchedKeyword {

    // 고유키
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 상품명
    @Column(name = "product_name", nullable = false)
    private String productName;

    // 한 페이지에 나오는 상품에 대한 갯수
    @Column(name = "keyword_count", nullable = false)
    private Integer keywordCount;

}
