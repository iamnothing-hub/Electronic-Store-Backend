package com.electronistore.service;

import com.electronistore.dto.CategoryDto;
import com.electronistore.dto.PageableResponse;


import java.util.ArrayList;

public interface CategoryService {

//    Create
    CategoryDto createCategory(CategoryDto categoryDto);

//    update
    CategoryDto updateCategory(CategoryDto categoryDto, String id);
//    delete
    void deleteCategory(String id);
//    get all
    PageableResponse<CategoryDto> getAllCategories(int pageNumber, int pageSize, String sortBy, String sortDir);
//    get single
    CategoryDto getSingleCategory(String id);
//    search
    ArrayList<CategoryDto> searchCategory(String key);
}
