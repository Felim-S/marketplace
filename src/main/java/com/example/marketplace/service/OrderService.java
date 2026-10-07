package com.example.marketplace.service;

import com.example.marketplace.model.BasketItem;
import com.example.marketplace.model.Order;
import com.example.marketplace.model.OrderItem;
import com.example.marketplace.model.User;
import com.example.marketplace.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getOrdersForUser(User user) {
        return orderRepository.getOrdersByUserId(user.getId());
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public void updateOrderStatus(int orderId, String status) {
        orderRepository.findById(orderId).ifPresent(order -> {
            order.setStatus(status);
            orderRepository.save(order);
        });
    }

    public void makeOrder(int userId, List<BasketItem> basketItems) {

        double total = basketItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() *  item.getQuantity())
                .sum();

        Order order = new Order();
        order.setUserId(userId);
        order.setTotal(total);

        List<OrderItem> orderItems = new ArrayList<>();
        for (BasketItem item : basketItems) {

            OrderItem orderItem = new OrderItem();
            orderItem.setProductName(item.getProduct().getName());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPriceAtPurchase(item.getProduct().getPrice());
            orderItem.setOrder(order);

            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);
        orderRepository.save(order);

    }
}
