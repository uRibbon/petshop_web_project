package com.dog.shop.domain.crawling;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PopularSearchedKeyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "상품명")
    private String product_name;

    @Column(name = "한 페이지에 나오는 상품 이름의 갯수")
    private Integer keyword_count;

}
