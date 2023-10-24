package com.dog.shop.repository.popularSearchedKeyword;

import com.dog.shop.domain.popularSearchedKeyword.PopularSearchedKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PopularKeywordRepository extends JpaRepository<PopularSearchedKeyword, Integer> {


}
