package com.electronistore.service.impl;

import com.electronistore.dto.CategoryDto;
import com.electronistore.dto.PageableResponse;
import com.electronistore.entity.Category;
import com.electronistore.exception.ResourceNotFoundException;
import com.electronistore.helper.PageHelper;
import com.electronistore.repository.CategoryRepository;
import com.electronistore.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;


@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper mapper;   // ye enitity se Dto aur Dto se entity mein convert karega

//    Create Category
    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
//        Create CategoryId
        String categoryId = UUID.randomUUID().toString();
        categoryDto.setCategoryId(categoryId);
        Category category = mapper.map(categoryDto, Category.class);
        Category savedCategory = categoryRepository.save(category);
        return mapper.map(savedCategory, CategoryDto.class);   //-> ye categoryDto return karega
    }

//    Update Category
    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, String id) {
//      get category by Id
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category is not found !!"));
//       Update Category one by one;
        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setCoverImage(categoryDto.getCoverImage());
//        Save updated category in repository
        Category save = categoryRepository.save(category);
        return mapper.map(save, CategoryDto.class);
    }

//    Delete Category
    @Override
    public void  deleteCategory(String id) {
//        get category by Id
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category is not found !!"));
        categoryRepository.delete(category);

    }


//  Get all Categories
    @Override
    public PageableResponse<CategoryDto> getAllCategories(int pageNumber, int pageSize, String sortBy, String sortDir) {

//        Sorting ke liye Ham Sort Class liye hain aur isme ham ternary operator use kiye hain.
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable =  PageRequest.of(pageNumber,pageSize,sort);
        Page<Category> page = categoryRepository.findAll(pageable);
        PageableResponse<CategoryDto> pageableResponse = PageHelper.getPageableResponse(page, CategoryDto.class);   // PageHelper Class helper package se aaya hai.
        return pageableResponse;
    }

//    Get Single Categories
    @Override
    public CategoryDto getSingleCategory(String id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category is not found!!"));
        return mapper.map(category, CategoryDto.class);
    }

    // TODO: 10-12-2024
//    Search Category by Keys
    @Override
    public ArrayList<CategoryDto> searchCategory(String keyword) {

        ArrayList<Category> categoryList = categoryRepository.findBytitleContaining(keyword);
        ArrayList<CategoryDto> dtos = new ArrayList<>();
        categoryList.forEach((Category ct) -> {
            CategoryDto dto = mapper.map(ct, CategoryDto.class);
            dtos.add(dto);
        } );
        return dtos ;
    }
}
