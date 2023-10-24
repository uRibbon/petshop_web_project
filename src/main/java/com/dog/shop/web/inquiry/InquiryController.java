package com.dog.shop.web.inquiry;

import com.dog.shop.domain.inquiry.reqDTO.InquiryReqDTO;
import com.dog.shop.domain.inquiry.resDTO.InquiryResDTO;
import com.dog.shop.service.inquiry.InquiryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inquiry")
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryService inquiryService;

    private final ModelMapper modelMapper;

    // 등록
    @PostMapping
    public InquiryResDTO regInquiry(@RequestBody InquiryReqDTO inquiryReqDTO) {
        return inquiryService.regInquiry(inquiryReqDTO);
    }

}
