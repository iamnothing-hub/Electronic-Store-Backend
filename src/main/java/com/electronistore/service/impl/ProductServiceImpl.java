package com.electronistore.service.impl;

import com.electronistore.dto.CategoryDto;
import com.electronistore.dto.PageableResponse;
import com.electronistore.dto.ProductDto;
import com.electronistore.entity.Category;
import com.electronistore.entity.Product;
import com.electronistore.exception.ResourceNotFoundException;
import com.electronistore.helper.PageHelper;
import com.electronistore.repository.CategoryRepository;
import com.electronistore.repository.ProductRepository;
import com.electronistore.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;
@Transactional
@Service("productServiceImpl")
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        String productId = UUID.randomUUID().toString();
        productDto.setProductId(productId);
        productDto.setAddedDate(new Date());
        Product product = productRepository.save(mapper.map(productDto, Product.class));
        return mapper.map(product,ProductDto.class);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto,String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product is not found!!"));

        product.setProductTitle(productDto.getProductTitle());
        product.setDescription(productDto.getDescription());
        product.setColor(productDto.getColor());
        product.setPrice(productDto.getPrice());
        product.setDiscount(productDto.getDiscount());
        product.setIsLive(productDto.getIsLive());
        product.setQuantity(productDto.getQuantity());
        product.setProductImageName(productDto.getProductImageName());
        product.setWarranty(productDto.getWarranty());
        product.setIsFreeDelivery(productDto.getIsFreeDelivery());
        Product product1 = productRepository.save(product);
        return mapper.map(product1, ProductDto.class);
    }

    @Override
    public void deleteProduct(String id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product is not found!!"));
        productRepository.delete(product);
    }

    @Override
    public ProductDto getSingleProduct(String id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product is not found!!"));
        return mapper.map(product,ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllProducts(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending()) ;

        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> products = productRepository.findAll(pageable);
        return PageHelper.getPageableResponse(products, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getLiveProducts(Boolean isLive, int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending()) ;

        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> products = productRepository.findByIsLiveTrue(pageable);
        return PageHelper.getPageableResponse(products, ProductDto.class);


    }

    @Override
    public PageableResponse<ProductDto> getProductBySubTitle(String subtitle, int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending()) ;

        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> products = productRepository.findByProductTitleContaining(subtitle,pageable);
        return PageHelper.getPageableResponse(products, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getProductsByKeyword(String keyword, int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending()) ;

        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> products = productRepository.findByDescriptionContaining(keyword,pageable);
        return PageHelper.getPageableResponse(products, ProductDto.class);
    }

    @Override
    public ProductDto createProductWithCategory(ProductDto productDto, String categoryId) {

        // fetch category by id
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not Found !!"));

        String productId = UUID.randomUUID().toString();
        productDto.setProductId(productId);
        productDto.setAddedDate(new Date());
        productDto.setCategory(mapper.map(category, CategoryDto.class));
        Product product = productRepository.save(mapper.map(productDto, Product.class));
        return mapper.map(product,ProductDto.class);

        //todo: is method ko CategoryController ke ander banayenge kyunki URI /categories se start ho rha hai.


    }

    @Override
    public ProductDto updateCategoryOfProduct(String productId, String categoryId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product is not found!!"));
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category is not Found !!"));
        product.setCategory(category);
        Product save = productRepository.save(product);
        return mapper.map(save, ProductDto.class);

        //todo: is method ko CategoryController ke ander banayenge kyunki URI /categories se start ho rha hai.
    }

    @Override
    public PageableResponse<ProductDto> getAllProductsOfCategory(String categoryId, int pageNumber, int pageSize, String sortBy, String sortDir) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category is not Found !!"));

        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending()) ;
        Pageable pageable = PageRequest.of(pageNumber,pageSize, sort);
        Page<Product> productPage = productRepository.findByCategory(category, pageable);

        return PageHelper.getPageableResponse(productPage,ProductDto.class);

        //todo: is method ko CategoryController ke ander banayenge kyunki URI /categories se start ho rha hai.
    }

}
