package com.electronistore.repository;

import com.electronistore.entity.Order;
import com.electronistore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {

    List<Order> findByUser(User user);
}
