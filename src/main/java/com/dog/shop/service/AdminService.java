package com.dog.shop.service;

import com.dog.shop.domain.product.Product;
import com.dog.shop.errorcode.ErrorCode;
import com.dog.shop.exception.CommonException;
import com.dog.shop.myenum.SalesStatus;
import com.dog.shop.product.dto.ProductReqDTO;
import com.dog.shop.product.dto.ProductReqForm;
import com.dog.shop.product.dto.ProductResDTO;
import com.dog.shop.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class AdminService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    // 상품등록
    public ProductResDTO saveProduct(ProductReqDTO productReqDTO) {
        Product product = modelMapper.map(productReqDTO,Product.class);
        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct,ProductResDTO.class);
    }

    // 상품 이미지 등록
    public String saveImage(MultipartFile file) {
        // 서버에서 이미지를 저장할 경로 설정
        String uploadDir = "/var/www/html/main/";

        // 유니크한 파일 이름 생성 (필요에 따라 로직을 수정할 수 있음)
        String originalFileName = file.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

        // 전체 파일 경로 생성
        String filePath = uploadDir + uniqueFileName;

        // 디렉터리가 존재하는지 확인
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // 이미지 저장
        try {
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("이미지 저장에 실패했습니다.");
        }

        // 이미지에 접근할 수 있는 경로 또는 URL 반환 (필요에 따라 수정)
        return "http://yourserver.com/main/" + uniqueFileName;
    }

    // 수정
    public void updateProduct(ProductReqForm productReqForm) {
        Product existProduct = productRepository.findById(productReqForm.getId())
                .orElseThrow(() ->
                        new CommonException(ErrorCode.NON_LOGIN, HttpStatus.NOT_FOUND));
        existProduct.setDescription(productReqForm.getDescription());
        existProduct.setPrice(productReqForm.getPrice());
        existProduct.setProductName(productReqForm.getProductName());

        SalesStatus salesStatus = SalesStatus.valueOf(productReqForm.getSalesStatus().toUpperCase());
        existProduct.setSalesStatus(salesStatus);

        existProduct.setStock(productReqForm.getStock());
    }

    // 상품 삭제
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new CommonException(ErrorCode.NON_LOGIN,HttpStatus.NOT_FOUND));
        productRepository.delete(product);
    }
}
