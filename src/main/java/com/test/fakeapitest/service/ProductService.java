package com.test.fakeapitest.service;


import com.test.fakeapitest.domain.Product;
import com.test.fakeapitest.dto.AddProductDto;
import com.test.fakeapitest.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public Product addProduct(AddProductDto addProductDto){



        // 카테고리 추가해줘야 함
        Product product = new Product();

        product.setProductTitle(addProductDto.getTitle());
        product.setPrice(addProductDto.getPrice());
        product.setDescription(addProductDto.getDescription());
        return productRepository.save(product);
    }

    @Transactional
    public Page<Product> getProducts(Long categoryId, int page, int pageSize){

        return productRepository.findProductByCategory_id(categoryId, PageRequest.of(page, pageSize));
    }


    @Transactional
    public Product getProduct(Long id){

        return productRepository.findById(id).orElseThrow();
    }



}
