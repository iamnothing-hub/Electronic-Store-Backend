package com.electronistore.controller;

import com.electronistore.payload.ApiResponseMessage;
import com.electronistore.dto.PageableResponse;
import com.electronistore.dto.ProductDto;
import com.electronistore.payload.ImageResponse;
import com.electronistore.service.ImageService;
import com.electronistore.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/products")
//@CrossOrigin(origins = "http://localhost:3000")
@Tag(name="Product Controller", description = "REST APIs for Product Controller Operations")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ImageService imageService;

    @Value("${product.image.path}")
    private String productImagePath;

    private Logger logger = LoggerFactory.getLogger(ProductController.class);

    //    create product
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto productDto) {
        ProductDto product = productService.createProduct(productDto);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    ;

    //    update product
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@Valid @RequestBody ProductDto productDto, @PathVariable String productId) {

        ProductDto product = productService.updateProduct(productDto, productId);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    ;

    //    delete product
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponseMessage> deleteProduct(@PathVariable String productId) {
        productService.deleteProduct(productId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder().message("Product is deleted successfully!!")
                .status(HttpStatus.OK)
                .success(true)
                .build();

        return new ResponseEntity<>(responseMessage, HttpStatus.OK);


    }

    ;

    //    get single product
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getSingleProduct(@PathVariable String productId) {
        ProductDto product = productService.getSingleProduct(productId);
        return new ResponseEntity<>(product, HttpStatus.OK);

    }

    ;

    //    get all products
    @GetMapping
    public ResponseEntity<PageableResponse<ProductDto>> getAllProducts(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "productTitle", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
        PageableResponse<ProductDto> products = productService.getAllProducts(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(products, HttpStatus.OK);

    }

    ;

    //    get all products Live
    @GetMapping("/live")
    public ResponseEntity<PageableResponse<ProductDto>> getLiveProducts(
            Boolean isLive,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "productTitle", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
        PageableResponse<ProductDto> products = productService.getLiveProducts(isLive, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(products, HttpStatus.OK);

    }

    ;

    //    Search product by subtitle
    @GetMapping("/search/{subtitle}")
    public ResponseEntity<PageableResponse<ProductDto>> getProductBySubTitle(
            @PathVariable String subtitle,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "productTitle", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
        PageableResponse<ProductDto> products = productService.getProductBySubTitle(subtitle, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(products, HttpStatus.OK);

    }

    ;

    //    Search product by Keywords
    @GetMapping("/search/feature/{keyword}")
    public ResponseEntity<PageableResponse<ProductDto>> getProductsByKeyword(
            @PathVariable String keyword,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "productTitle", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
        PageableResponse<ProductDto> products = productService.getProductsByKeyword(keyword, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(products, HttpStatus.OK);

    }


    // Add product image
    @PostMapping("/image/{productId}")
    public ResponseEntity<ImageResponse> uploadProductImage(@PathVariable String productId, @RequestParam(value = "productImage")MultipartFile file) throws IOException {
        // Yahan par ham image ko uoload karenge
        String imageName = imageService.uploadImage(file, productImagePath);
        logger.info("Product Image name{}: ",imageName);
        // Fir us porticular product ko id k through get karenge
        ProductDto productDto = productService.getSingleProduct(productId);
//todo        logger.info("1. Product details{}: ",productDto);
        // product image ko product mein set karke product ko update karenge
        productDto.setProductImageName(imageName);
        logger.info("2. Product details{}: ",productDto);
        ProductDto dto = productService.updateProduct(productDto, productId);
        // Yahan par ham image upload ke liye ek response create kar rhe hain
        ImageResponse response = ImageResponse.builder()
                .imageName(imageName)
                .message("Product image added successfully !!")
                .success(true)
                .status(HttpStatus.CREATED)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Serve product image
    @GetMapping("/image/{productId}")
    public void serveUserImage(@PathVariable String productId, HttpServletResponse response) throws IOException {
        ProductDto product = productService.getSingleProduct(productId);
        logger.info("Product Detail {}: ", product);
        InputStream resource = imageService.getResource(productImagePath, product.getProductImageName());

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);

        StreamUtils.copy(resource,response.getOutputStream());

    }
}
