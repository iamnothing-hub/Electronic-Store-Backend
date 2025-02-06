package com.electronistore.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name="cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartItemId;

    private int quantity;

    private int totalPrice;

    @ManyToOne
    @JoinColumn(name="product_id", unique = false)
    @JsonIgnoreProperties({"cartItem", "category"})  // Prevent recursion at Product
    private Product product;

    // Mapping Cart
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    @JsonBackReference  // Prevent circular reference while serializing
//    @JsonIgnore  // Prevent circular reference during serialization
    @JsonIgnoreProperties("items")  // Ignore the items property in Cart during CartItem serialization
    private Cart cart;
}
