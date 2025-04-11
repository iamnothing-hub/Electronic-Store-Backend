package com.electronistore.service;

import com.electronistore.dto.PageableResponse;
import com.electronistore.dto.ProductDto;
import com.electronistore.entity.Category;
import com.electronistore.entity.Product;
import com.electronistore.repository.CategoryRepository;
import com.electronistore.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ModelMapper mapper;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private CategoryRepository categoryRepository;


    private Product product;
    private Category category;
    @BeforeEach
    public void init(){
        category = Category.builder().categoryId("1234").title("Smartphone").description("In this category there is premium segment phone collection").build();
       product = Product.builder()
                .productId("abcd")
                .description("This is best phone in this segment")
                .productTitle("Samsung Note 14 ultra")
                .price(14000)
                .quantity(99)
                .discount(15)
                .color(List.of("red","white","black"))
                .addedDate(new Date())
                .isLive(true)
                .isFreeDelivery(false)
                .warranty(6)
                .inStock(false)
                .category(category)
                .build();
    }

    @Test
    public void createProductTest(){
        Mockito.when(productRepository.save(Mockito.any())).thenReturn(product);
        ProductDto dto = productService.createProduct(mapper.map(product, ProductDto.class));
        Assertions.assertNotNull(dto);
        Assertions.assertEquals(dto.getProductTitle(),product.getProductTitle(), "Product title is not same");
    }

    @Test
    public void updateProductTest(){
        String productId = "abcd";

        ProductDto productDto = ProductDto.builder()
                .description("This is best phone in this segment")
                .productTitle("Samsung S 25+ ultra")
                .price(14000)
                .quantity(99)
                .discount(15)
                .color(List.of("red","white","black"))
                .addedDate(new Date())
                .isLive(true)
                .isFreeDelivery(false)
                .warranty(6)
                .inStock(false)
                .build();
        Mockito.when(productRepository.findById(Mockito.anyString())).thenReturn(Optional.of(product));
        Mockito.when(productRepository.save(Mockito.any())).thenReturn(product);
        ProductDto productDto1 = productService.updateProduct(productDto, productId);

        Assertions.assertNotNull(productDto1);
        Assertions.assertEquals(productDto1.getProductTitle(), productDto.getProductTitle(), "Product Title is not same");
    }

    @Test
    public void deleteProductTest(){
        String productId = "absdc";

        Mockito.when(productRepository.findById(Mockito.anyString())).thenReturn(Optional.of(product));
        productService.deleteProduct(productId);
        Mockito.verify(productRepository, Mockito.times(1)).delete(product);
    }

    @Test
    public void getSingleProductTest(){
        String productId = "absdc";

        Mockito.when(productRepository.findById(Mockito.anyString())).thenReturn(Optional.of(product));

        ProductDto singleProduct = productService.getSingleProduct(productId);

        Assertions.assertNotNull(singleProduct);
        Assertions.assertEquals(singleProduct.getProductTitle(),product.getProductTitle(),"Product is not matched. Please give correct product id");
    }

    @Test
    public void getAllProductsTest(){

        Product product1 = Product.builder()
                .productId("abcdfs")
                .description("This is best phone in this segment")
                .productTitle("Samsung Note 14 ultra")
                .price(14000)
                .quantity(99)
                .discount(15)
                .color(List.of("red","white","black"))
                .addedDate(new Date())
                .isLive(true)
                .isFreeDelivery(false)
                .warranty(6)
                .inStock(false)
                .category(category)
                .build();
        Product product2 = Product.builder()
                .productId("abcdfsadc")
                .description("This is best phone in this segment")
                .productTitle("Samsung Note 14 ultra")
                .price(14000)
                .quantity(99)
                .discount(15)
                .color(List.of("red","white","black"))
                .addedDate(new Date())
                .isLive(true)
                .isFreeDelivery(false)
                .warranty(6)
                .inStock(false)
                .category(category)
                .build();
        List<Product> productList = List.of(product,product1,product2);

        Page<Product> page = new PageImpl(productList);

        Mockito.when(productRepository.findAll((Pageable) Mockito.any())).thenReturn(page);
        PageableResponse<ProductDto> allProducts = productService.getAllProducts(5, 10, "title", "desc");
        Assertions.assertEquals(3,allProducts.getContent().size());
    }

    @Test
    public void getAllProductsOfCategoryTest1(){
        String categoryId = "1234";

        Mockito.when(categoryRepository.findById(Mockito.anyString())).thenReturn(Optional.ofNullable(category));


        Product product1 = Product.builder()
                .productId("abcdfs")
                .description("This is best phone in this segment")
                .productTitle("Samsung Note 14 ultra")
                .price(14000)
                .quantity(99)
                .discount(15)
                .color(List.of("red","white","black"))
                .addedDate(new Date())
                .isLive(true)
                .isFreeDelivery(false)
                .warranty(6)
                .inStock(false)
                .category(category)
                .build();
        Product product2 = Product.builder()
                .productId("abcdfsadc")
                .description("This is best phone in this segment")
                .productTitle("Samsung Note 14 ultra")
                .price(14000)
                .quantity(99)
                .discount(15)
                .color(List.of("red","white","black"))
                .addedDate(new Date())
                .isLive(true)
                .isFreeDelivery(false)
                .warranty(6)
                .inStock(false)
                .category(category)
                .build();
        List<Product> productList = List.of(product,product1,product2);

        Page<Product> page = new PageImpl<>(productList);

        Mockito.when(productRepository.findByCategory(Mockito.any(),Mockito.any())).thenReturn(page);

        PageableResponse<ProductDto> allProductsOfCategory = productService.getAllProductsOfCategory(categoryId, 3, 10, "title", "asc");

        Assertions.assertEquals(3, allProductsOfCategory.getContent().size());
    }

    @Test
    public void getLiveProductsTest(){
        Product product1 = Product.builder()
                .productId("abcdfs")
                .description("This is best phone in this segment")
                .productTitle("Samsung Note 14 ultra")
                .price(14000)
                .quantity(99)
                .discount(15)
                .color(List.of("red","white","black"))
                .addedDate(new Date())
                .isLive(true)
                .isFreeDelivery(false)
                .warranty(6)
                .inStock(false)
                .category(category)
                .build();
        Product product2 = Product.builder()
                .productId("abcdfsadc")
                .description("This is best phone in this segment")
                .productTitle("Samsung Note 14 ultra")
                .price(14000)
                .quantity(99)
                .discount(15)
                .color(List.of("red","white","black"))
                .addedDate(new Date())
                .isLive(true)
                .isFreeDelivery(false)
                .warranty(6)
                .inStock(false)
                .category(category)
                .build();
        List<Product> productList = List.of(product,product1,product2);
        Page<Product> page = new PageImpl(productList);
        Mockito.when(productRepository.findByIsLiveTrue(Mockito.any())).thenReturn(page);

        PageableResponse<ProductDto> liveProducts = productService.getLiveProducts(true, 5, 10, "title", "desc");

        Assertions.assertEquals(3, liveProducts.getContent().size());
    }

    @Test
    public void getLiveProductsTest1(){
        Product product1 = Product.builder()
                .productId("abcdfs")
                .description("This is best phone in this segment")
                .productTitle("Samsung Note 14 ultra")
                .price(14000)
                .quantity(99)
                .discount(15)
                .color(List.of("red","white","black"))
                .addedDate(new Date())
                .isLive(true)
                .isFreeDelivery(false)
                .warranty(6)
                .inStock(false)
                .category(category)
                .build();
        Product product2 = Product.builder()
                .productId("abcdfsadc")
                .description("This is best phone in this segment")
                .productTitle("Samsung Note 14 ultra")
                .price(14000)
                .quantity(99)
                .discount(15)
                .color(List.of("red","white","black"))
                .addedDate(new Date())
                .isLive(true)
                .isFreeDelivery(false)
                .warranty(6)
                .inStock(false)
                .category(category)
                .build();
        List<Product> productList = List.of(product,product1,product2);
        Page<Product> page = new PageImpl(productList);
        Mockito.when(productRepository.findByIsLiveTrue(Mockito.any())).thenReturn(page);

        PageableResponse<ProductDto> liveProducts = productService.getLiveProducts(true, 5, 10, "title", "asc");

        Assertions.assertEquals(3, liveProducts.getContent().size());
    }

    @Test
    public void getProductBySubTitleTest(){

        Product product1 = Product.builder()
                .productId("abcdfs")
                .description("This is best phone in this segment")
                .productTitle("Samsung Note 14 ultra")
                .price(14000)
                .quantity(99)
                .discount(15)
                .color(List.of("red","white","black"))
                .addedDate(new Date())
                .isLive(true)
                .isFreeDelivery(false)
                .warranty(6)
                .inStock(false)
                .category(category)
                .build();
        Product product2 = Product.builder()
                .productId("abcdfsadc")
                .description("This is best phone in this segment")
                .productTitle("Samsung Note 14 ultra")
                .price(14000)
                .quantity(99)
                .discount(15)
                .color(List.of("red","white","black"))
                .addedDate(new Date())
                .isLive(true)
                .isFreeDelivery(false)
                .warranty(6)
                .inStock(false)
                .category(category)
                .build();
        List<Product> productList = List.of(product,product1,product2);

        Page<Product> page = new PageImpl<>(productList);

        Mockito.when(productRepository.findByProductTitleContaining(Mockito.anyString(), Mockito.any())).thenReturn(page);
        PageableResponse<ProductDto> productBySubTitle = productService.getProductBySubTitle("phone", 3, 10, "title", "desc");

        Assertions.assertEquals(3, productBySubTitle.getContent().size());
    }

    @Test
    public void getProductBySubTitleTest1(){

        Product product1 = Product.builder()
                .productId("abcdfs")
                .description("This is best phone in this segment")
                .productTitle("Samsung Note 14 ultra")
                .price(14000)
                .quantity(99)
                .discount(15)
                .color(List.of("red","white","black"))
                .addedDate(new Date())
                .isLive(true)
                .isFreeDelivery(false)
                .warranty(6)
                .inStock(false)
                .category(category)
                .build();
        Product product2 = Product.builder()
                .productId("abcdfsadc")
                .description("This is best phone in this segment")
                .productTitle("Samsung Note 14 ultra")
                .price(14000)
                .quantity(99)
                .discount(15)
                .color(List.of("red","white","black"))
                .addedDate(new Date())
                .isLive(true)
                .isFreeDelivery(false)
                .warranty(6)
                .inStock(false)
                .category(category)
                .build();
        List<Product> productList = List.of(product,product1,product2);

        Page<Product> page = new PageImpl<>(productList);

        Mockito.when(productRepository.findByProductTitleContaining(Mockito.anyString(), Mockito.any())).thenReturn(page);
        PageableResponse<ProductDto> productBySubTitle = productService.getProductBySubTitle("phone", 3, 10, "title", "asc");

        Assertions.assertEquals(3, productBySubTitle.getContent().size());
    }

    @Test
    public void getProductsByKeywordTest(){

        Product product1 = Product.builder()
                .productId("abcdfs")
                .description("This is best phone in this segment")
                .productTitle("Samsung Note 14 ultra")
                .price(14000)
                .quantity(99)
                .discount(15)
                .color(List.of("red","white","black"))
                .addedDate(new Date())
                .isLive(true)
                .isFreeDelivery(false)
                .warranty(6)
                .inStock(false)
                .category(category)
                .build();
        Product product2 = Product.builder()
                .productId("abcdfsadc")
                .description("This is best phone in this segment")
                .productTitle("Samsung Note 14 ultra")
                .price(14000)
                .quantity(99)
                .discount(15)
                .color(List.of("red","white","black"))
                .addedDate(new Date())
                .isLive(true)
                .isFreeDelivery(false)
                .warranty(6)
                .inStock(false)
                .category(category)
                .build();


        List<Product> productList = List.of(product,product1,product2);

        Page<Product> page = new PageImpl<>(productList);
        Mockito.when(productRepository.findByDescriptionContaining(Mockito.anyString(), Mockito.any())).thenReturn(page);

        PageableResponse<ProductDto> productsByKeyword = productService.getProductsByKeyword("abcd", 3, 10, "title", "desc");

        Assertions.assertEquals(3,productsByKeyword.getContent().size());

    }

    @Test
    public void getProductsByKeywordTest1(){

        Product product1 = Product.builder()
                .productId("abcdfs")
                .description("This is best phone in this segment")
                .productTitle("Samsung Note 14 ultra")
                .price(14000)
                .quantity(99)
                .discount(15)
                .color(List.of("red","white","black"))
                .addedDate(new Date())
                .isLive(true)
                .isFreeDelivery(false)
                .warranty(6)
                .inStock(false)
                .category(category)
                .build();
        Product product2 = Product.builder()
                .productId("abcdfsadc")
                .description("This is best phone in this segment")
                .productTitle("Samsung Note 14 ultra")
                .price(14000)
                .quantity(99)
                .discount(15)
                .color(List.of("red","white","black"))
                .addedDate(new Date())
                .isLive(true)
                .isFreeDelivery(false)
                .warranty(6)
                .inStock(false)
                .category(category)
                .build();


        List<Product> productList = List.of(product,product1,product2);

        Page<Product> page = new PageImpl<>(productList);
        Mockito.when(productRepository.findByDescriptionContaining(Mockito.anyString(), Mockito.any())).thenReturn(page);

        PageableResponse<ProductDto> productsByKeyword = productService.getProductsByKeyword("abcd", 3, 10, "title", "asc");

        Assertions.assertEquals(3,productsByKeyword.getContent().size());

    }

    @Test
    public void createProductWithCategoryTest(){

        String categoryId = "abdcs";

        Mockito.when(categoryRepository.findById(Mockito.anyString())).thenReturn(Optional.of(category));

        Mockito.when(productRepository.save(Mockito.any())).thenReturn(product);

        ProductDto productWithCategory = productService.createProductWithCategory(mapper.map(product, ProductDto.class), categoryId);

        Assertions.assertNotNull(productWithCategory);
        Assertions.assertEquals(productWithCategory.getCategory().getTitle(), product.getCategory().getTitle());
    }

    @Test
    public void updateCategoryOfProductTest(){
        String productId = "abcds";
        String categoryId = "1234";

        Mockito.when(productRepository.findById(Mockito.anyString())).thenReturn(Optional.of(product));
        Mockito.when(categoryRepository.findById(Mockito.anyString())).thenReturn(Optional.of(category));

        Mockito.when(productRepository.save(Mockito.any())).thenReturn(product);
        ProductDto productDto = productService.updateCategoryOfProduct(productId, categoryId);

        Assertions.assertNotNull(productDto);

    }

    @Test
    public void getAllProductsOfCategoryTest(){
        String categoryId = "1234";

        Mockito.when(categoryRepository.findById(Mockito.anyString())).thenReturn(Optional.ofNullable(category));


        Product product1 = Product.builder()
                .productId("abcdfs")
                .description("This is best phone in this segment")
                .productTitle("Samsung Note 14 ultra")
                .price(14000)
                .quantity(99)
                .discount(15)
                .color(List.of("red","white","black"))
                .addedDate(new Date())
                .isLive(true)
                .isFreeDelivery(false)
                .warranty(6)
                .inStock(false)
                .category(category)
                .build();
        Product product2 = Product.builder()
                .productId("abcdfsadc")
                .description("This is best phone in this segment")
                .productTitle("Samsung Note 14 ultra")
                .price(14000)
                .quantity(99)
                .discount(15)
                .color(List.of("red","white","black"))
                .addedDate(new Date())
                .isLive(true)
                .isFreeDelivery(false)
                .warranty(6)
                .inStock(false)
                .category(category)
                .build();
        List<Product> productList = List.of(product,product1,product2);

        Page<Product> page = new PageImpl<>(productList);

        Mockito.when(productRepository.findByCategory(Mockito.any(),Mockito.any())).thenReturn(page);

        PageableResponse<ProductDto> allProductsOfCategory = productService.getAllProductsOfCategory(categoryId, 3, 10, "title", "desc");

        Assertions.assertEquals(3, allProductsOfCategory.getContent().size());
    }

    @Test
    public void getAllProductsOfCategoryTest2(){
        String categoryId = "1234";

        Mockito.when(categoryRepository.findById(Mockito.anyString())).thenReturn(Optional.ofNullable(category));


        Product product1 = Product.builder()
                .productId("abcdfs")
                .description("This is best phone in this segment")
                .productTitle("Samsung Note 14 ultra")
                .price(14000)
                .quantity(99)
                .discount(15)
                .color(List.of("red","white","black"))
                .addedDate(new Date())
                .isLive(true)
                .isFreeDelivery(false)
                .warranty(6)
                .inStock(false)
                .category(category)
                .build();
        Product product2 = Product.builder()
                .productId("abcdfsadc")
                .description("This is best phone in this segment")
                .productTitle("Samsung Note 14 ultra")
                .price(14000)
                .quantity(99)
                .discount(15)
                .color(List.of("red","white","black"))
                .addedDate(new Date())
                .isLive(true)
                .isFreeDelivery(false)
                .warranty(6)
                .inStock(false)
                .category(category)
                .build();
        List<Product> productList = List.of(product,product1,product2);

        Page<Product> page = new PageImpl<>(productList);

        Mockito.when(productRepository.findByCategory(Mockito.any(),Mockito.any())).thenReturn(page);

        PageableResponse<ProductDto> allProductsOfCategory = productService.getAllProductsOfCategory(categoryId, 3, 10, "title", "asc");

        Assertions.assertEquals(3, allProductsOfCategory.getContent().size());
    }
}
