package com.dog.shop.web.inquiry;

import com.dog.shop.dto.inquiryDto.InquiryReqDTO;
import com.dog.shop.dto.inquiryDto.InquiryResDTO;
import com.dog.shop.service.inquiry.InquiryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/inquiry")
@RequiredArgsConstructor
public class InquiryRestController {

    private final InquiryService inquiryService;

    private final ModelMapper modelMapper;


    // 등록
    @ResponseBody
    @PostMapping
    public ResponseEntity<InquiryResDTO> registerInquiry(@RequestBody InquiryReqDTO inquiryReqDTO) {
        // userId 값을 2L로 고정
        Long userId = 2L;
        Long productId = 2L;

        InquiryResDTO result = inquiryService.regInquiry(userId, productId, inquiryReqDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // 전체 조회
    @GetMapping
    @ResponseBody
    public List<InquiryResDTO> getInquiryies() {
        return inquiryService.getInquiries();
    }

    // id로 개별 조회
    @GetMapping("/{id}")
    @ResponseBody
    public InquiryResDTO getInquiryById(@PathVariable Long id) {
        return inquiryService.getInquiryById(id);
    }

    // 삭제
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteInquiry(@PathVariable Long id) {
        inquiryService.deleteInquiry(id);
        return ResponseEntity.ok(id + " 번째 문의글이 삭제처리 되었습니다.");
    }

    // 수정
    @PatchMapping("/{id}")
    @ResponseBody
    public InquiryResDTO updateInquiry(@PathVariable Long id, @RequestBody InquiryReqDTO inquiryReqDTO) {
        return inquiryService.updateInquiry(id, inquiryReqDTO);
    }
}
