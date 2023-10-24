package com.dog.shop.service;

import com.dog.shop.domain.Product;
import com.dog.shop.dto.ProductResDTO;
import com.dog.shop.errorcode.ErrorCode;
import com.dog.shop.exception.CommonException;
import com.dog.shop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public Page<ProductResDTO> findAllProducts(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        return products.map(product -> modelMapper.map(product, ProductResDTO.class));
    }

    @Transactional(readOnly = true)
    public ProductResDTO getProductById(Long id) {
        Product productEntity = productRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.NON_LOGIN, HttpStatus.NOT_FOUND));
        ProductResDTO productResDTO = modelMapper.map(productEntity,ProductResDTO.class);
        return productResDTO;
    }



}
