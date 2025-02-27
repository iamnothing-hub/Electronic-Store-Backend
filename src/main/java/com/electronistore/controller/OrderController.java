package com.electronistore.controller;

import com.electronistore.config.AppConstants;
import com.electronistore.dto.CreateOrderRequest;
import com.electronistore.dto.OrderDto;
import com.electronistore.dto.PageableResponse;
import com.electronistore.payload.ApiResponseMessage;
import com.electronistore.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@Tag(name = "Order Controller", description = "REST APIs for Order Controller Operations")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Create Order
    @PreAuthorize("hasAnyRole('"+ AppConstants.ROLE_ADMIN+"','"+ AppConstants.ROLE_NORMAL+"')")
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody CreateOrderRequest orderDto){
        OrderDto order = orderService.createOrder(orderDto);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    //Remove Order
    @PreAuthorize("hasRole('"+ AppConstants.ROLE_ADMIN+"')")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponseMessage> removeOrder(@PathVariable String orderId){
        orderService.removeOrder(orderId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder().message("Order has been deleted successfully").status(HttpStatus.OK).success(true).build();
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    //Get orders of user
    @PreAuthorize("hasAnyRole('"+ AppConstants.ROLE_ADMIN+"','"+ AppConstants.ROLE_NORMAL+"')")
    @GetMapping("/users/{userId}")
    public ResponseEntity<List<OrderDto>> getOrderOfUser(@PathVariable String userId){
        List<OrderDto> orders = orderService.getOrderOfUser(userId);
        return new ResponseEntity<>(orders, HttpStatus.OK);

    }

    //Get orders
    @PreAuthorize("hasRole('"+ AppConstants.ROLE_ADMIN+"')")
    @GetMapping
    public ResponseEntity<PageableResponse<OrderDto>> getOrders(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "orderDate", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ){
        PageableResponse<OrderDto> orders = orderService.getOrders(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}
