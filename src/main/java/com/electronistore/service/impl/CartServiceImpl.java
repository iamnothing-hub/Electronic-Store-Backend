package com.electronistore.service.impl;

import com.electronistore.dto.AddItemToCartRequest;
import com.electronistore.dto.CartDto;
import com.electronistore.entity.Cart;
import com.electronistore.entity.CartItem;
import com.electronistore.entity.Product;
import com.electronistore.entity.User;
import com.electronistore.exception.BadRequestException;
import com.electronistore.exception.ResourceNotFoundException;
import com.electronistore.repository.CartItemRepository;
import com.electronistore.repository.CartRepository;
import com.electronistore.repository.ProductRepository;
import com.electronistore.repository.UserRepository;
import com.electronistore.service.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl  implements CartService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CartItemRepository cartItemRepository;


    @Override
    public CartDto addItemsToCart(String userId, AddItemToCartRequest request) {

        String productId = request.getProductId();
        int quantity = request.getQuantity();

        // Check condition
        if (quantity <= 0) {
            throw new BadRequestException("Quantity should not be less than zero !!");
        }
        // Fetch Product from productRepository by productId;
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product is not found!!"));

        // Fetch User from userRepository

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User is not found!!"));

        // Fetch Cart by the help of user

        Cart cart = null;

        /* todo
               Agar user cart available hai to cart aa jayega nahi to user ke liye new cart banega
         */
        try{
            cart = cartRepository.findByUser(user).get();
        }
        catch (NoSuchElementException e){
            cart = new Cart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreatedAt(new Date());
          //  cart.setUser(user);           // Associate the user to the cart
        //    cartRepository.save(cart);    // Save the new cart to the database
        }




        // Perform Cart Operations
        // If product is already present in cart then

        AtomicReference<Boolean> isUpdate = new AtomicReference<>(false);
        List<CartItem> items = cart.getItems();

        // Ye ek ek product ko iterate karke match karegi
        List<CartItem> updatedItems = items.stream().map(item -> {
            if (item.getProduct().getProductId().equals(productId)) {
                // product is present in cart
                // here we will only update the cart
                item.setQuantity(quantity);
                item.setTotalPrice(quantity * product.getPrice());

                // Ham iteration wale method like map mein kisi bhi variable ko direct update nahi kar payenge.
                // Iske liye AtomicReference class hoti hai jo automatic data update karti hai.
                isUpdate.set(true);
//                return item;
            }
            return item;
        }).collect(Collectors.toList());

        cart.setItems(updatedItems);

        //todo Create new items
        if(!isUpdate.get()){
            CartItem cartItem = CartItem.builder()
                    .quantity(quantity)
                    .totalPrice(quantity * product.getPrice())
                    .product(product)
                    .cart(cart)
                    .build();
            cart.getItems().add(cartItem);
        }


        cart.setUser(user);
        Cart updatedCart = cartRepository.save(cart);

        return mapper.map(updatedCart, CartDto.class);





        /*  new Technique */

        /**

        // Perform Cart Operations
        boolean isUpdate = false;
        List<CartItem> updatedItems = new ArrayList<>();

        for (CartItem item : cart.getItems()) {
            if (item.getProduct().getProductId().equals(productId)) {
                // Product is already in cart, update the quantity and total price
                item.setQuantity(quantity);
                item.setTotalPrice(quantity * product.getPrice());
                updatedItems.add(item);
                isUpdate = true;
            } else {
                updatedItems.add(item);
            }
        }

        // If the product was not found in the cart, create a new CartItem
        if (!isUpdate) {
            CartItem cartItem = new CartItem();
            cartItem.setQuantity(quantity);
            cartItem.setTotalPrice(quantity * product.getPrice());
            cartItem.setProduct(product);
            cartItem.setCart(cart);  // Ensure the cart is set in the CartItem
            updatedItems.add(cartItem);
        }

        // Update the cart's items list
        cart.setItems(updatedItems);
        Cart updatedCart = cartRepository.save(cart);  // Save the updated cart with items

        return mapper.map(updatedCart, CartDto.class);

         **/
    }

    @Override
    public void removeItemFromCart(String userId, int cartItem) {
        CartItem cartItem1 = cartItemRepository.findById(cartItem).orElseThrow(() -> new ResourceNotFoundException("CartItem is not found"));

        if(cartItem1.getCart().getUser().getUserId().equals(userId))
            cartItemRepository.delete(cartItem1);

        else
            throw new BadRequestException("userId is not matched!!");
    }

    @Override
    public void clearCart(String userId) {

        // Fetch user
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found !!"));
        // Fetch Cart
        Cart cart = cartRepository.findByUser(user).orElseThrow(()-> new ResourceNotFoundException("User is not found in the cart !!"));

        cart.getItems().clear();
        cartRepository.save(cart);
    }

    @Override
    public CartDto getCartByUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User is not found !!"));

        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart is not found"));
        return mapper.map(cart, CartDto.class);
    }
}
