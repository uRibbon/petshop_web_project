package com.dog.shop.repository.crawling;

import com.dog.shop.domain.crawling.PopularSearchedKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PopularKeywordRepository extends JpaRepository<PopularSearchedKeyword, Integer> {


}
