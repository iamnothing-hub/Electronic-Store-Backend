package com.electronistore.dto;

import com.electronistore.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddItemToCartRequest {

    private String productId;

    private int quantity;

}
