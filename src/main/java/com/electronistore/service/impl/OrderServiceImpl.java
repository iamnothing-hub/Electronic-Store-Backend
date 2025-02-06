package com.electronistore.service.impl;

import com.electronistore.dto.CreateOrderRequest;
import com.electronistore.dto.OrderDto;
import com.electronistore.dto.PageableResponse;
import com.electronistore.entity.*;
import com.electronistore.exception.BadRequestException;
import com.electronistore.exception.ResourceNotFoundException;
import com.electronistore.helper.PageHelper;
import com.electronistore.repository.CartRepository;
import com.electronistore.repository.OrderRepository;
import com.electronistore.repository.UserRepository;
import com.electronistore.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ModelMapper mapper;


    @Override
    public OrderDto createOrder(CreateOrderRequest orderDto) {

        String userId = orderDto.getUserId();
        String cartId = orderDto.getCartId();
        // Fetch User
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User is not Found!!"));
        // Fetch Cart
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart is not created!!"));

        // Find cart item from cart
        List<CartItem> items = cart.getItems();
        if(items.size()<=0){
            throw new BadRequestException("Invalid number of Items in Cart !!");
        }

        // build order from orderDto
        Order order = Order.builder()
                .billingPhone(orderDto.getBillingPhone())
                .deliveredDate(null)
                .orderDate(new Date())
                .orderId(UUID.randomUUID().toString())
                .orderStatus(orderDto.getOrderStatus())
                .billingAddress(orderDto.getBillingAddress())
                .billingName(orderDto.getBillingName())
                .paymentStatus(orderDto.getPaymentStatus())
                .user(user)
                .build();
                // We have not set order Amount, Order items

        // Convert cart items to order items to set in order
        AtomicReference<Integer> orderAmount = new AtomicReference<>(0);
        List<OrderItem> orderItems = items.stream().map(item -> {
//            CartItem -> OrderItem
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .quantity(item.getCartItemId())
                    .totalPrice(item.getQuantity() * item.getProduct().getPrice())    //todo Doubt Hai Yahan pe
                    .product(item.getProduct())
                    .build();
            // order amount ko add kar rhe hain.
            orderAmount.set(orderAmount.get() + orderItem.getTotalPrice());
            return orderItem;
        }).collect(Collectors.toList());

        order.setOrderItemList(orderItems);
        order.setOrderAmount(orderAmount.get());

        // clear cart
        cart.getItems().clear();
        cartRepository.save(cart);
        Order savedOrder = orderRepository.save(order);

        return mapper.map(savedOrder, OrderDto.class);
    }

    @Override
    public void removeOrder(String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order is not Found !!"));
        orderRepository.delete(order);

    }

    @Override
    public List<OrderDto> getOrderOfUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User is not found for getting order"));
        List<Order> orderList = orderRepository.findByUser(user);
        //Change order to orderDto
        List<OrderDto> orderDtos = orderList.stream().map(order -> mapper.map(order, OrderDto.class)).collect(Collectors.toList());
        return orderDtos;
    }

    @Override
    public PageableResponse<OrderDto> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Order> orders = orderRepository.findAll(pageable);

        return PageHelper.getPageableResponse(orders,OrderDto.class);
    }
}
