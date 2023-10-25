package com.dog.shop.web.popularSearchedKeyword;

import com.dog.shop.domain.popularSearchedKeyword.resDTO.PopularSearchedKeywordResDTO;
import com.dog.shop.service.popularKeyword.PopularKeywordService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/keywords")
@RequiredArgsConstructor
public class PopularKeywordRestController {

    private final PopularKeywordService popularKeywordService;

    private final ModelMapper modelMapper;


    // 전체 조회
    @GetMapping
    public List<PopularSearchedKeywordResDTO> getResults() {
        return popularKeywordService.getResult();
    }
}
