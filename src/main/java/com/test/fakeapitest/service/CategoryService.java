package com.test.fakeapitest.service;


import com.test.fakeapitest.domain.Category;
import com.test.fakeapitest.dto.AddCategoryDto;
import com.test.fakeapitest.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public Category addCategory(AddCategoryDto addCategoryDto){

        Category category = new Category();
        category.setName(addCategoryDto.getName());
        return categoryRepository.save(category);
    }
    @Transactional
    public List<Category> getCategories() {

        return categoryRepository.findAll();
    }
    @Transactional
    public Category getCategory(Long categoryId){

        return categoryRepository.findById(categoryId).orElseThrow();
    }
}
