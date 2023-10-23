package com.dog.shop.product.service;

import com.dog.shop.domain.Product;
import com.dog.shop.product.dto.ProductReqDTO;
import com.dog.shop.product.dto.ProductResDTO;
import com.dog.shop.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

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
}
