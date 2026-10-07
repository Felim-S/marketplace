package com.example.marketplace.repository;

import com.example.marketplace.model.BasketItem;
import com.example.marketplace.model.Product;
import com.example.marketplace.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface BasketItemRepository extends JpaRepository<BasketItem,Integer> {
    Optional<BasketItem> findByUserAndProduct(User user, Product product);
    List<BasketItem> findByUser(User user);
    @Transactional
    void deleteByUser(User user);
    @Transactional
    void deleteByProduct(Product product);
}
