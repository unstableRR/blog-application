package com.blogapplication.services;

import com.blogapplication.payloads.CategoryDto;

import java.util.List;

public interface CategoryService {

    //create
    CategoryDto createCategory(CategoryDto categoryDto);

    //update
    CategoryDto updateCategory(CategoryDto categoryDto, Integer catId);

    //delete
    void deleteCategory(Integer catId);

    //get
    CategoryDto getCategory(Integer catId);

    //getAll
    List<CategoryDto> getCategories();
}
