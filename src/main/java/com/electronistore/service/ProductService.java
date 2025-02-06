package com.electronistore.service;

import com.electronistore.dto.PageableResponse;
import com.electronistore.dto.ProductDto;

import java.util.List;

public interface ProductService {

//    create product
    ProductDto createProduct(ProductDto productDto);
//    update product
    ProductDto updateProduct(ProductDto productDto, String productId);
//    delete product
    void deleteProduct(String id);

//    get single product
    ProductDto getSingleProduct(String id);

//    get all products
    PageableResponse<ProductDto> getAllProducts(int pageNumber, int pageSize, String sortBy, String sortDir);

//    get all products Live
    PageableResponse<ProductDto> getLiveProducts(Boolean isLive, int pageNumber, int pageSize, String sortBy, String sortDir);

//    Search product by subtitle
    PageableResponse<ProductDto> getProductBySubTitle(String subtitle, int pageNumber, int pageSize, String sortBy, String sortDir);

//    Search product by Keywords
    PageableResponse<ProductDto> getProductsByKeyword(String keyword, int pageNumber, int pageSize, String sortBy, String sortDir);

//    Create product with category
    ProductDto createProductWithCategory(ProductDto productDto, String categoryId);

//    Update Category of the product
    ProductDto updateCategoryOfProduct(String productId, String categoryId);

//    Get all products of the Category
    PageableResponse<ProductDto> getAllProductsOfCategory(String categoryId, int pageNumber, int pageSize, String sortBy, String sortDir);
}
