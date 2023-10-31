package com.dog.shop.web.inquiry;

import com.dog.shop.domain.product.Product;
import com.dog.shop.dto.inquiryDto.InquiryReqDTO;
import com.dog.shop.dto.inquiryDto.InquiryResDTO;
import com.dog.shop.product.repository.ProductRepository;
import com.dog.shop.service.inquiry.InquiryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/inquiry")
@RequiredArgsConstructor
public class InquiryRestController {

    private final InquiryService inquiryService;

    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;


    // 상품 문의하기 페이지
    @ResponseBody
    @GetMapping("/register/{id}") // productId를 가져오면 됨
    public String registerInquiryPage(@PathVariable Long id, Model model) {
        // 상품 ID를 사용하여 데이터베이스에서 해당 상품 정보를 가져옵니다.
        Product product = productRepository.findById(id).orElseThrow(); // TODO 에러처리 필요
        model.addAttribute("product", product);
        // 문의 DTO 객체를 초기화하고 모델에 추가합니다.
        InquiryReqDTO inquiryDTO = new InquiryReqDTO();
        model.addAttribute("inquiryForm", inquiryDTO);
        return "inquiryRegister"; // 상품 문의 페이지로 리디렉션
    }

    // 등록
    @ResponseBody
    @PostMapping("/submit/inquiry")
    public String registerInquiry(@ModelAttribute InquiryReqDTO inquiryReqDTO, Model model) {
        // userId 값을 2L로 고정
        Long userId = 2L;
        Long productId = 2L;

        InquiryResDTO result = inquiryService.regInquiry(userId, productId, inquiryReqDTO);

        // 결과를 모델에 추가하고 뷰를 반환할 수 있습니다.
        model.addAttribute("result", result);

        // 예: 'inquiryResult'라는 이름의 뷰로 이동합니다.
        return "redirect:/";
    }
    /*ResponseBody
    @PostMapping
    public ResponseEntity<InquiryResDTO> registerInquiry(@RequestBody InquiryReqDTO inquiryReqDTO) {
        // userId 값을 2L로 고정
        Long userId = 2L;
        Long productId = 2L;

        InquiryResDTO result = inquiryService.regInquiry(userId, productId, inquiryReqDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }*/

    // 전체 조회
    @GetMapping
    public String getInquiryies(Model model) {
        List<InquiryResDTO> inquiries = inquiryService.getInquiries();
        model.addAttribute("inquiries", inquiries);
        return "inquiryCheck";
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
