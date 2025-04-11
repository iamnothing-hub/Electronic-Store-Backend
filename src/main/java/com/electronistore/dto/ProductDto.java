package com.electronistore.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ProductDto {

    private String productId;
    @NotBlank(message = "Product name should not be empty!!")
    private String productTitle;
    @Length(min = 10, message = "Description should be atleast 10 Charecters!!")
    private String description;

    private List<String> color;

    private int price;

    private int discount;

    private int quantity;

    private Boolean inStock;

    private Boolean isLive;

    private Boolean isFreeDelivery;

    private Date addedDate;

    private int warranty;

    private String productImageName;

    private CategoryDto category;

}
