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
}
