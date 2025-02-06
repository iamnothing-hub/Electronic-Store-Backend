package com.electronistore.dto;

import com.electronistore.entity.CartItem;
import com.electronistore.entity.User;
import lombok.*;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CartDto {
    private String cartId;

    private Date createdAt;


    private User user;

    // Mapping with Cart-Item
    private List<CartItem> items = new ArrayList<>();
}
