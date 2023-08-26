package com.test.fakeapitest.controller;


import com.test.fakeapitest.domain.Product;
import com.test.fakeapitest.dto.AddProductDto;
import com.test.fakeapitest.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {


    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Product addProduct(@RequestBody AddProductDto addProductDto){


        return productService.addProduct(addProductDto);
    }


    @GetMapping
    public Page<Product> getProducts(@RequestParam Long categoryId, @RequestParam(required = false, defaultValue = "0") int page){

        int pageSize = 0;

        return productService.getProducts(categoryId, page, pageSize);
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id){
        return productService.getProduct(id);
    }


}
