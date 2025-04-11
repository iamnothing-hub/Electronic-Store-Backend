package com.electronistore.service;

import com.electronistore.dto.CreateOrderRequest;
import com.electronistore.entity.Order;
import com.electronistore.repository.CartRepository;
import com.electronistore.repository.OrderRepository;
import com.electronistore.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class OrderServiceTest {

    @MockBean
    private OrderRepository orderRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private CartRepository cartRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ModelMapper mapper;

    private Order order;

    private CreateOrderRequest request;

}
