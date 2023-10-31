package com.dog.shop.product.service;

import com.dog.shop.domain.product.Product;
import com.dog.shop.errorcode.ErrorCode;
import com.dog.shop.exception.CommonException;
import com.dog.shop.product.dto.ProductReqDTO;
import com.dog.shop.product.dto.ProductResDTO;
import com.dog.shop.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
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
        System.out.println(productReqDTO);
        Product product = modelMapper.map(productReqDTO, Product.class);
        log.info("Description " + product.getDescription() +
                "Price " + product.getPrice() +
                "Id " + product.getId() +
                "Sales_status" + product.getSalesStatus() +
                "Stock " + product.getStock());
        log.info("Des == {}", product.getProductName());
        Product savedProduct = productRepository.save(product);
        System.out.println("savedProduct = " + savedProduct.toString());
        return modelMapper.map(savedProduct, ProductResDTO.class);
    }

    @Transactional(readOnly = true)
    public ProductResDTO getProductById(Long id) {
        Product productEntity = productRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.NON_LOGIN, HttpStatus.NOT_FOUND));
        ProductResDTO productResDTO = modelMapper.map(productEntity, ProductResDTO.class);
        return productResDTO;
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.NON_LOGIN, HttpStatus.NOT_FOUND));
        productRepository.delete(product);
    }

    // 이미지를 저장하고 URL을 반환하는 메서드

    /*
     * public String saveImage(MultipartFile file) {
     * // 이미지를 저장할 디렉토리 경로 설정 (로컬경로)
     * // 처음 노트북로컬경로
     * // String uploadDir =
     * "C:/Users/heosu/Documents/GitHub/petshop_web_project/src/main/resources/static/images/";
     * // 처음 데스크탑로컬경로
     * // String uploadDir =
     * "C:/Users/user/Documents/GitHub/petshop_web_project/src/main/resources/static/images/";
     * //원격 주소
     * String uploadDir = "http://49.50.165.98/main/";
     * 
     * // 파일 이름 생성 (현준님요청으로 url path하고 image의 fileName만 나오게)
     * String fileName = uploadDir + file.getOriginalFilename();
     * 
     * // 파일 저장 경로 설정
     * String filePath = fileName;
     * 
     * // 밑에서 경로를 짜르고 파일이름만 가져가기전에 우리가사용할 url path + fileName 변수에담기
     * 
     * // 이미지 저장
     * try {
     * file.transferTo(new File(filePath));
     * } catch (IOException e) {
     * e.printStackTrace();
     * // 이미지 저장 실패 시 예외 처리
     * throw new RuntimeException("Failed to store the image");
     * }
     * 
     * // 이미지 파일의 경로를 반환
     * return filePath;
     * }
     */

    public String saveImage(MultipartFile file) {
        // 서버에서 이미지를 저장할 경로 설정
        String uploadDir = "/var/www/html/main/";

        // 유니크한 파일 이름 생성 (필요에 따라 로직을 수정할 수 있음)
        String originalFileName = file.getOriginalFilename();
         String fileExtension =
         originalFileName.substring(originalFileName.lastIndexOf("."));
         String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

        // 전체 파일 경로 생성
        String filePath = uploadDir + originalFileName;

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
        return "http://49.50.165.98/main/" + uniqueFileName;
    }

    /*
     * public String saveImage(MultipartFile file) {
     * // 실제 파일 시스템 경로
     * String uploadDir = "/var/www/html/main";
     * 
     * // 원본 파일 이름에서 확장자 추출
     * String extension =
     * file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(
     * "."));
     * 
     * // UUID를 사용하여 유일한 파일 이름 생성
     * String uniqueFileName = UUID.randomUUID().toString() + extension;
     * 
     * // 파일 저장 경로 설정
     * String filePath = Paths.get(uploadDir, uniqueFileName).toString();
     * 
     * // 디렉토리가 존재하는지 확인하고, 존재하지 않으면 생성
     * File dir = new File(uploadDir);
     * if (!dir.exists()) {
     * if (!dir.mkdirs()) {
     * throw new RuntimeException("Failed to create directory " + uploadDir);
     * }
     * }
     * 
     * // 이미지 저장
     * try {
     * file.transferTo(new File(filePath));
     * } catch (IOException e) {
     * e.printStackTrace();
     * // 이미지 저장 실패 시 예외 처리
     * throw new RuntimeException("Failed to store the image");
     * }
     * 
     * // 이미지 파일의 이름을 반환 (전체 경로가 아닌 파일 이름만)
     * return uniqueFileName;
     * }
     */
}
