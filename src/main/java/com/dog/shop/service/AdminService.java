package com.dog.shop.service;

import com.dog.shop.domain.product.Product;
import com.dog.shop.errorcode.ErrorCode;
import com.dog.shop.exception.CommonException;
import com.dog.shop.myenum.SalesStatus;
import com.dog.shop.product.dto.ProductReqForm;
import com.dog.shop.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class AdminService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    @org.springframework.transaction.annotation.Transactional
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
}
