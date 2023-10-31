package com.dog.shop.web.popularSearchedKeyword;

import com.dog.shop.domain.popularSearchedKeyword.resDTO.PopularSearchedKeywordResDTO;
import com.dog.shop.service.popularKeyword.PopularKeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/keywords")
@RequiredArgsConstructor
public class PopularKeywordController {

    private final PopularKeywordService popularKeywordService;

    // 인기 검색어 보여주기
    @GetMapping("/list")
    public ModelAndView keywordList() {
        List<PopularSearchedKeywordResDTO> popularSearchedKeywordResDTOList = popularKeywordService.getResult();
        return new ModelAndView("/popularSearchedKeyword/popular-searched-keyword-list", "keywords", popularSearchedKeywordResDTOList);

    }
}
