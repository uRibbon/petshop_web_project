package com.dog.shop.service.inquiry;

import com.dog.shop.domain.inquiry.Inquiry;
import com.dog.shop.domain.inquiry.reqDTO.InquiryReqDTO;
import com.dog.shop.domain.inquiry.resDTO.InquiryResDTO;
import com.dog.shop.repository.inquiry.InquiryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Book;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository inquiryRepository;

    private final ModelMapper modelMapper;

    // 등록
    public InquiryResDTO regInquiry(InquiryReqDTO inquiryReqDTO) {
        Inquiry inquiry = modelMapper.map(inquiryReqDTO, Inquiry.class);
        Inquiry savedInquiry = inquiryRepository.save(inquiry);
        return modelMapper.map(savedInquiry, InquiryResDTO.class);
    }


}
