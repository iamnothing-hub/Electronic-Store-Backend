package com.electronistore.service;

import com.electronistore.dto.CategoryDto;
import com.electronistore.dto.PageableResponse;
import com.electronistore.entity.Category;
import com.electronistore.exception.ResourceNotFoundException;
import com.electronistore.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper mapper;   // ye enitity se Dto aur Dto se entity mein convert karega

    @Override
    public CategoryDto create(CategoryDto categoryDto) {

        Category category = mapper.map(categoryDto, Category.class);
        Category savedCategory = categoryRepository.save(category);
        return mapper.map(savedCategory, CategoryDto.class);   //-> ye categoryDto return karega
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto, String id) {
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

    @Override
    public void  delete(String id) {
//        get category by Id
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category is not found !!"));
        categoryRepository.delete(category);

    }

    @Override
    public PageableResponse<CategoryDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {

        categoryRepository.findAll();
        return null;
    }

    @Override
    public CategoryDto getSingle(String id) {
        return null;
    }

    @Override
    public ArrayList<CategoryDto> search(String key) {
        return null;
    }
}
