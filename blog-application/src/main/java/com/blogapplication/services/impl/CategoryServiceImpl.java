package com.blogapplication.services.impl;

import com.blogapplication.entities.Category;
import com.blogapplication.exceptions.ResourceNotFoundException;
import com.blogapplication.payloads.CategoryDto;
import com.blogapplication.repositories.CategoryRepo;
import com.blogapplication.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepo catRepo;

    @Autowired
    private ModelMapper mm;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {

        Category cat = this.mm.map(categoryDto, Category.class);
        Category addedCat = this.catRepo.save(cat);

        return this.mm.map(addedCat,CategoryDto.class);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Integer catId) {
       Category cat = this.catRepo.findById(catId)
               .orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", catId));

       cat.setCategoryDesc(categoryDto.getCategoryDesc());
       cat.setCategoryTitle(categoryDto.getCategoryTitle());

       Category updatedCat = this.catRepo.save(cat);
       return this.mm.map(updatedCat, CategoryDto.class);
    }

    @Override
    public void deleteCategory(Integer catId) {

        Category cat = this.catRepo.findById(catId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", catId));

        this.catRepo.delete(cat);

    }

    @Override
    public CategoryDto getCategory(Integer catId) {

        Category cat = this.catRepo.findById(catId)
                .orElseThrow(() -> new ResourceNotFoundException("Category","Category Id",catId));

        return this.mm.map(cat, CategoryDto.class);
    }

    @Override
    public List<CategoryDto> getCategories() {

        List<Category> categories = this.catRepo.findAll();
        List<CategoryDto> catDtos = categories.stream()
                .map((cat) -> this.mm.map(cat, CategoryDto.class)).collect(Collectors.toList());

        return catDtos;
    }
}
