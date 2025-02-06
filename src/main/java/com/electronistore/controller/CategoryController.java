package com.electronistore.controller;

import com.electronistore.dto.ProductDto;
import com.electronistore.payload.ApiResponseMessage;
import com.electronistore.dto.CategoryDto;
import com.electronistore.dto.PageableResponse;
import com.electronistore.service.CategoryService;
import com.electronistore.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

//   Create
    @PostMapping("/category")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto dto){
        CategoryDto category = categoryService.createCategory(dto);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

//    Update
    @PutMapping("/{categoryId}")
    public  ResponseEntity<CategoryDto> updateCategory( @RequestBody CategoryDto categoryDto, @PathVariable String categoryId){
        CategoryDto dto = categoryService.updateCategory(categoryDto, categoryId);
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }
//    Delete
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable String categoryId){
        categoryService.deleteCategory(categoryId);
        ApiResponseMessage message = ApiResponseMessage.builder()
                .message("Category is deleted successfully!!")
                .status(HttpStatus.OK)
                .success(true)
                .build();
        return new ResponseEntity<>(message,HttpStatus.OK);
    }

//    Get All
    @GetMapping
    public ResponseEntity<PageableResponse<CategoryDto>> getAllCategory(
            @RequestParam(value = "pageNumber",defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc", required = false) String sortDir){
        PageableResponse<CategoryDto> categories = categoryService.getAllCategories(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(categories,HttpStatus.OK);
    }

//    Get single
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable String categoryId){
        CategoryDto category = categoryService.getSingleCategory(categoryId);
        return new ResponseEntity<>(category,HttpStatus.OK);
    }

//    Search
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<CategoryDto>> searchCategory(@PathVariable String keyword){
        ArrayList<CategoryDto> categoryDtos = categoryService.searchCategory(keyword);
        return new ResponseEntity<>(categoryDtos,HttpStatus.OK);
    }

    // create product with category
    @PostMapping("/{categoryId}/products")
    public  ResponseEntity<ProductDto> createProductWithCategory(
            @Valid @RequestBody ProductDto productDto,
            @PathVariable String categoryId){

        ProductDto product = productService.createProductWithCategory(productDto, categoryId);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    // update category of the product
    @PutMapping("/{categoryId}/products/{productId}")
    public  ResponseEntity<ProductDto> updateCategoryOfProduct(@PathVariable String productId,@PathVariable String categoryId){
        ProductDto productDto = productService.updateCategoryOfProduct(productId, categoryId);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    // find products by Category
    @GetMapping("/product/{categoryId}")
    public ResponseEntity<PageableResponse> getAllProductsOfCategory(
            @PathVariable String categoryId,
            @RequestParam(value = "pageNumber",defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "category", required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc", required = false) String sortDir
    ){
        PageableResponse<ProductDto> allProductsOfCategory = productService.getAllProductsOfCategory(categoryId, pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(allProductsOfCategory, HttpStatus.OK);
    }
}
