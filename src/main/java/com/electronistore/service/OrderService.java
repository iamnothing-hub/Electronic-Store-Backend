package com.electronistore.service;

import com.electronistore.dto.CreateOrderRequest;
import com.electronistore.dto.OrderDto;
import com.electronistore.dto.PageableResponse;

import java.util.List;

public interface OrderService {

    // Create Order
    OrderDto createOrder(CreateOrderRequest orderDto);

    //Remove Order
    void removeOrder(String orderId);

    //Get orders of user
    List<OrderDto> getOrderOfUser(String userId);

    //Get orders
    PageableResponse<OrderDto> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir);
}
