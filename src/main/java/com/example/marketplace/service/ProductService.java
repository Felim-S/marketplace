package com.example.marketplace.service;

import com.example.marketplace.model.Product;
import com.example.marketplace.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final BasketService basketService;

    public ProductService(ProductRepository productRepository, BasketService basketService) {
        this.productRepository = productRepository;
        this.basketService = basketService;
    }

    public Optional<Product> getProductById(int id) {
        return productRepository.findById(id);
    }

    public Product createProduct(String name, String description, double price, String imageUrl) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setImageUrl(imageUrl);
        productRepository.save(product);

        return product;
    }

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public List<Product> getAllVisibleProducts() {
        return productRepository.findAllByHiddenFalse();
    }

    public void updateProduct(int id, String name, String description, double price, String imageUrl) {
        productRepository.findById(id).ifPresent(product -> {
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setImageUrl(imageUrl);
            productRepository.save(product);
        });
    }

    public void toggleHidden(int id) {
        productRepository.findById(id).ifPresent(product -> {
            if (!product.isHidden()) {
                basketService.clearBasketItemsForProduct(product);
            }
            product.setHidden(!product.isHidden());
            productRepository.save(product);
        });
    }

    public void deleteProduct(int id) {
        productRepository.findById(id).ifPresent(
                product -> {
                    basketService.clearBasketItemsForProduct(product);
                    productRepository.delete(product);
                }
        );

    }

}
