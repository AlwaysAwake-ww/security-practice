package com.test.fakeapitest.controller;


import com.test.fakeapitest.domain.Category;
import com.test.fakeapitest.dto.AddCategoryDto;
import com.test.fakeapitest.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {


    private CategoryService categoryService;



    // 카테고리 추가
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Category addCategory(@RequestBody AddCategoryDto addCategoryDto){

        return categoryService.addCategory(addCategoryDto);
    }

    @GetMapping("/{categoryId}")
    public Category getCategory(@PathVariable Long categoryId){

        return categoryService.getCategory(categoryId);
    }

    @GetMapping
    public List<Category> getCategories(){

        return categoryService.getCategories();
    }
}
