package com.example.marketplace.controller;

import com.example.marketplace.model.Product;
import com.example.marketplace.model.User;
import com.example.marketplace.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final ProductService productService;

    public HomeController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public String showHome(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        List<Product> products = (user != null && user.isAdmin())
                ? productService.getAllProducts()
                : productService.getAllVisibleProducts();
        model.addAttribute("products", products);
        return "homepage";
    }

}