package com.dog.shop.product.service;

import com.dog.shop.domain.Product;
import com.dog.shop.product.dto.ProductResDTO;
import com.dog.shop.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper modelMapper;

    public Page<ProductResDTO> findAllProducts(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        return products.map(product -> modelMapper.map(product, ProductResDTO.class));
    }
}
