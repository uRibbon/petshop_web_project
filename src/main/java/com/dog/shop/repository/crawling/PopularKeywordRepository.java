package com.dog.shop.repository.crawling;

import com.dog.shop.domain.User;
import com.dog.shop.domain.crawling.PopularSearchedKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PopularKeywordRepository extends JpaRepository<PopularSearchedKeyword, Integer> {

}
