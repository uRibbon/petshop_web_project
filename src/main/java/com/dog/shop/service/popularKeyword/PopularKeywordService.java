package com.dog.shop.service.popularKeyword;

import com.dog.shop.domain.popularSearchedKeyword.PopularSearchedKeyword;
import com.dog.shop.domain.popularSearchedKeyword.resDTO.PopularSearchedKeywordResDTO;
import com.dog.shop.repository.popularSearchedKeyword.PopularKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import org.modelmapper.ModelMapper;
import static java.util.stream.Collectors.toList;

@Service
@Transactional
@RequiredArgsConstructor
public class PopularKeywordService {

    private final PopularKeywordRepository popularKeywordRepository;

    private final ModelMapper modelMapper;

    // 전체 조회(내림차순)
    @Transactional(readOnly = true)
    public List<PopularSearchedKeywordResDTO> getResult() {
        List<PopularSearchedKeyword> popularSearchedKeywordList = popularKeywordRepository.findAll(Sort.by(Sort.Direction.DESC, "keywordCount"));

        List<PopularSearchedKeywordResDTO> popularSearchedKeywordResDTOList = popularSearchedKeywordList.stream()
                .map(PopularSearchedKeyword -> modelMapper.map(PopularSearchedKeyword, PopularSearchedKeywordResDTO.class))
                .collect(toList());

        return popularSearchedKeywordResDTOList;
    }
}
