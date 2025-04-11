package com.electronistore.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
public class Product {

    @Id
    private String productId;

    private String productTitle;

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

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(/*name = "product_id"   (we will create foreign Key in product name with categoryId */ name = "category_id")
    @JsonIgnoreProperties("products")   // Ignore `products` in Category during serialization
    private Category category;


}
