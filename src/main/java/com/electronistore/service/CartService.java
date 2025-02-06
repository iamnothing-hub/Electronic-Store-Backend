package com.electronistore.service;

import com.electronistore.dto.AddItemToCartRequest;
import com.electronistore.dto.CartDto;
import com.electronistore.dto.ProductDto;

public interface CartService {

    // Add items to Cart
    // Case 1: If Cart is not available for user then we will create Cart and then add item to cart
    // Case 2: If Cart is available then add the items
    // Case 3: If Item is available in cart then we will increase the quantity of item.

    CartDto addItemsToCart(String userId, AddItemToCartRequest request);

    // Remove items from cart
    void removeItemFromCart(String userId, int cartItem);

    // Remove all items from cart
    void clearCart(String userId);

    CartDto getCartByUser(String userId);
}
