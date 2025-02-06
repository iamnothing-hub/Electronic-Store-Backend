package com.electronistore.controller;


import com.electronistore.dto.AddItemToCartRequest;
import com.electronistore.dto.CartDto;
import com.electronistore.payload.ApiResponseMessage;
import com.electronistore.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    // Add item to cart
    @PreAuthorize("hasAnyRole('ADMIN','NORMAL')")
    @PostMapping("/{userId}")
    public ResponseEntity<CartDto> addItemsToCart(@PathVariable String userId, @RequestBody AddItemToCartRequest request){
        CartDto cartDto = cartService.addItemsToCart(userId, request);
        return new ResponseEntity<>(cartDto, HttpStatus.CREATED);
    }

    // Remove items from cart
    @PreAuthorize("hasAnyRole('ADMIN','NORMAL')")
    @DeleteMapping("/{userId}/cart/{cartItem}")
    public ResponseEntity<ApiResponseMessage> removeItemFromCart(@PathVariable String userId, @PathVariable int cartItem){
        cartService.removeItemFromCart(userId,cartItem);
        ApiResponseMessage response = ApiResponseMessage.builder()
                .message("Item is removed")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Remove all items from cart
    @PreAuthorize("hasAnyRole('ADMIN','NORMAL')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> clearCart(@PathVariable String userId){
        cartService.clearCart(userId);
        ApiResponseMessage response = ApiResponseMessage.builder()
                .message("Cart is removed")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Get Item
    @PreAuthorize("hasAnyRole('ADMIN','NORMAL')")
    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCart( @PathVariable String userId){
        CartDto cart = cartService.getCartByUser(userId);
        return new ResponseEntity<>(cart, HttpStatus.OK);

    }
}
