package com.electronistore.service;

import com.electronistore.dto.CategoryDto;
import com.electronistore.dto.PageableResponse;


import java.util.ArrayList;

public interface CategoryService {

//    Create
    CategoryDto create(CategoryDto categoryDto);

//    update
    CategoryDto update(CategoryDto categoryDto, String id);
//    delete
    void delete(String id);
//    get all
    PageableResponse<CategoryDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir);
//    get single
    CategoryDto getSingle(String id);
//    search
    ArrayList<CategoryDto> search(String key);
}
