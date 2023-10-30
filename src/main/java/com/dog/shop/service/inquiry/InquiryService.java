package com.dog.shop.service.inquiry;

import com.dog.shop.domain.User;
import com.dog.shop.domain.inquiry.Inquiry;
import com.dog.shop.domain.product.Product;
import com.dog.shop.dto.inquiryDto.InquiryReqDTO;
import com.dog.shop.dto.inquiryDto.InquiryResDTO;
import com.dog.shop.exception.MemberNotFoundException;
import com.dog.shop.myenum.InquiryStatus;
import com.dog.shop.product.repository.ProductRepository;
import com.dog.shop.repository.UserRepository;
import com.dog.shop.repository.inquiry.InquiryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class InquiryService {
    private final ProductRepository productRepository;

    private final InquiryRepository inquiryRepository;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    // 등록
    public InquiryResDTO regInquiry(Long userId, Long productId, InquiryReqDTO inquiryReqDTO) {
        // User와 Product 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new MemberNotFoundException("user not found"));
        Product product = productRepository.findById(productId).orElseThrow(); // TODO 나중에 에러처리

        // InquiryReqDTO를 Inquiry로 변환
        Inquiry inquiry = modelMapper.map(inquiryReqDTO, Inquiry.class);
        inquiry.setUser(user);
        inquiry.setProduct(product);
        inquiry.setInquiryStatus(InquiryStatus.INCOMPLETED);

        // Inquiry 저장
        Inquiry savedInquiry = inquiryRepository.save(inquiry);

        // 저장된 Inquiry를 InquiryResDTO로 변환
        InquiryResDTO inquiryResDTO = modelMapper.map(savedInquiry, InquiryResDTO.class);
        inquiryResDTO.setEmail(user.getEmail());
        inquiryResDTO.setProductName(product.getProductName());

        return inquiryResDTO;
    }
    /*public InquiryResDTO regInquiry(Long userId, Long productId, InquiryReqDTO inquiryReqDTO) {

        Inquiry inquiry = new Inquiry(); // todo 에러 처리하기

        inquiry.setUser(userRepository.findById(userId).orElseThrow(() -> new MemberNotFoundException("user not found")));
        Inquiry savedInquiry = inquiryRepository.save(inquiry);
//        inquiry = modelMapper.map(inquiryReqDTO, Inquiry.class);


        InquiryResDTO inquiryResDTO = new InquiryResDTO();

        inquiry.setUser(userRepository.findById(userId).orElseThrow(() -> new MemberNotFoundException("user not found")));
        inquiry.setProduct(productRepository.findById(productId).orElseThrow());
        inquiryResDTO.setEmail(inquiry.getUser().getEmail());
        inquiryResDTO.setProductName(inquiry.getProduct().getProductName());
        inquiryResDTO.setTitle(inquiryReqDTO.getTitle());
        inquiryResDTO.setContent(inquiryReqDTO.getContent());
        inquiryResDTO.setInquiryStatus(InquiryStatus.INCOMPLETED);
        inquiryResDTO.setResponse(inquiry.getResponse());
        inquiryResDTO.setResponseDate(inquiry.getResponseDate());

        return modelMapper.map(inquiryResDTO, InquiryResDTO.class);
    }*/

    // 전체 조회
    @Transactional(readOnly = true)
    public List<InquiryResDTO> getInquiries() {
        List<Inquiry> inquiryList = inquiryRepository.findAll();

        List<InquiryResDTO> inquiryResDTOList = inquiryList.stream()
                .map(inquiry -> modelMapper.map(inquiry, InquiryResDTO.class))
                .collect(toList());

        return inquiryResDTOList;
    }

    // Id로 조회
    @Transactional(readOnly = true)
    public InquiryResDTO getInquiryById(Long id) {
        Inquiry inquiry = inquiryRepository.findById(id).orElseThrow();
        InquiryResDTO inquiryResDTO = modelMapper.map(inquiry, InquiryResDTO.class);
        return inquiryResDTO;
    }

    // 삭제
    public void deleteInquiry(Long id) {
        Inquiry inquiry = inquiryRepository.findById(id).orElseThrow();
        inquiryRepository.delete(inquiry);
    }

    // 수정
    public InquiryResDTO updateInquiry(Long id, InquiryReqDTO inquiryReqDTO) {
        Inquiry existInquiry = inquiryRepository.findById(id).orElseThrow();
        if(inquiryReqDTO.getTitle() != null) {
            existInquiry.setTitle(inquiryReqDTO.getTitle());
        }
        if(inquiryReqDTO.getContent() != null) {
            existInquiry.setContent(inquiryReqDTO.getContent());
        }
        return modelMapper.map(existInquiry, InquiryResDTO.class);
    }

}
