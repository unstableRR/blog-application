package com.blogapplication.controllers;

import com.blogapplication.entities.Category;
import com.blogapplication.payloads.ApiResponse;
import com.blogapplication.payloads.CategoryDto;
import com.blogapplication.services.impl.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    public CategoryServiceImpl catSer;

    //POST - create a user
    @PostMapping("/")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto catDto){
       CategoryDto createCatDto = this.catSer.createCategory(catDto);

       return new ResponseEntity<>(createCatDto, HttpStatus.OK);
    }

    //PUT - update a user
    @PutMapping("/{catId}")
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto catDto, @PathVariable Integer catId){
        CategoryDto updateCatDto = this.catSer.updateCategory(catDto, catId);

        return new ResponseEntity<>(updateCatDto, HttpStatus.OK);
    }

    //DELETE - delete a user
    @DeleteMapping("/{catId}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Integer catId){
        this.catSer.deleteCategory(catId);

        return new ResponseEntity<>(new ApiResponse("category deleted successfully !",true), HttpStatus.OK);
    }

    //GET - get a user
    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable Integer catId){
        CategoryDto cat = this.catSer.getCategory(catId);

        return new ResponseEntity<>(cat, HttpStatus.OK);
    }

    //GET - get all users
    @GetMapping("/")
    public ResponseEntity<List<CategoryDto>> getAllCategories(){
        List<CategoryDto> categories = this.catSer.getCategories();

        return ResponseEntity.ok(categories);

    }
}
