package com.example.marketplace.service;

import com.example.marketplace.model.BasketItem;
import com.example.marketplace.model.Product;
import com.example.marketplace.model.User;
import com.example.marketplace.repository.BasketItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class BasketService {

    private final BasketItemRepository basketItemRepository;

    public BasketService(BasketItemRepository basketItemRepository) {
        this.basketItemRepository = basketItemRepository;
    }

    public void addBasketItem(User user, Product product) {

        Optional<BasketItem> basketItem = basketItemRepository.findByUserAndProduct(user, product);

        if (basketItem.isPresent()) {
            basketItem.get().setQuantity(basketItem.get().getQuantity() + 1);
            basketItemRepository.save(basketItem.get());
        } else{

            BasketItem newBasketItem = new BasketItem();
            newBasketItem.setUser(user);
            newBasketItem.setProduct(product);
            newBasketItem.setQuantity(1);

            basketItemRepository.save(newBasketItem);

        }
    }

    public List<BasketItem> getBasketItemsForUser(User user) {
        return basketItemRepository.findByUser(user);
    }

    public void updateQuantity(User user, Product product, int delta) {
        Optional<BasketItem> basketItem = basketItemRepository.findByUserAndProduct(user, product);
        if (basketItem.isPresent()) {
            if (basketItem.get().getQuantity() + delta <= 0) {
                basketItemRepository.delete(basketItem.get());
            } else {
                basketItem.get().setQuantity(basketItem.get().getQuantity() + delta);
                basketItemRepository.save(basketItem.get());
            }
        }
    }

    public void removeItem(User user, Product product) {
        basketItemRepository.findByUserAndProduct(user, product)
                .ifPresent(item -> basketItemRepository.delete(item));
    }

    public void clearBasket(User user) {
        basketItemRepository.deleteByUser(user);
    }

    public void clearBasketItemsForProduct(Product product) {
        basketItemRepository.deleteByProduct(product);
    }
}
