package com.example.marketplace.repository;

import com.example.marketplace.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    public List<Order> getOrdersByUserId(Integer userId);
}
