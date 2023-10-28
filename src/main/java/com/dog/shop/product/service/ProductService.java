package com.dog.shop.product.service;

import com.dog.shop.domain.product.Product;
import com.dog.shop.errorcode.ErrorCode;
import com.dog.shop.exception.CommonException;
import com.dog.shop.myenum.SalesStatus;
import com.dog.shop.product.dto.ProductReqDTO;
import com.dog.shop.product.dto.ProductReqForm;
import com.dog.shop.product.dto.ProductResDTO;
import com.dog.shop.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public Page<ProductResDTO> findAllProducts(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        return products.map(product -> modelMapper.map(product, ProductResDTO.class));
    }

    public ProductResDTO saveProduct(ProductReqDTO productReqDTO) {
        Product product = modelMapper.map(productReqDTO,Product.class);
        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct,ProductResDTO.class);
    }

    @Transactional(readOnly = true)
    public ProductResDTO getProductById(Long id) {
        Product productEntity = productRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.NON_LOGIN, HttpStatus.NOT_FOUND));
        ProductResDTO productResDTO = modelMapper.map(productEntity,ProductResDTO.class);
        return productResDTO;
    }
    @Transactional
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

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new CommonException(ErrorCode.NON_LOGIN,HttpStatus.NOT_FOUND));
        productRepository.delete(product);
    }

    // 이미지를 저장하고 URL을 반환하는 메서드
    public String saveImage(MultipartFile file) {
        // 이미지를 저장할 디렉토리 경로 설정 (로컬경로)
        // 처음 로컬경로
//         String uploadDir = "C:/Users/heosu/Documents/GitHub/petshop_web_project/src/main/resources/static/images/";
        //원격 주소
        String uploadDir = "49.50.165.98/main/";

        // 파일 이름 생성 (현준님요청으로 url path하고 image의 fileName만 나오게)
        String fileName = uploadDir + file.getOriginalFilename();

        // 파일 저장 경로 설정
        String filePath = fileName;

        // 밑에서 경로를 짜르고 파일이름만 가져가기전에 우리가사용할 url path + fileName 변수에담기

        // 이미지 저장
        try {
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            // 이미지 저장 실패 시 예외 처리
            throw new RuntimeException("Failed to store the image");
        }

        // 이미지 파일의 경로를 반환
        return filePath;
    }
}
