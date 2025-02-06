package com.electronistore.dto;

import com.electronistore.entity.Product;
import lombok.*;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CartItemDto {
    private int cartItemId;

    private int quantity;

    private int totalPrice;

    private Product product;

}
