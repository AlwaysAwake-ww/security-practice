package com.test.fakeapitest.repository;

import com.test.fakeapitest.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Override
    Optional<Product> findById(Long aLong);

    Page<Product> findProductByCategory_id(Long categoryId, Pageable pageable);
}
