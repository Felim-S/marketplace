package com.example.marketplace.controller;

import com.example.marketplace.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/product-details")
    public String showProductDetails(@RequestParam int id, Model model) {
        var product = productService.getProductById(id);
        if (product.isEmpty()) return "redirect:/";
        model.addAttribute("product", product.get());
        return "product-details";
    }

    @GetMapping("/admin-add-product")
    public String showAdminAddProduct() {
        return "admin-add-product";
    }

    @PostMapping("/admin-add-product")
    public String handleAddNewProduct(@RequestParam String name,
                                      @RequestParam String description,
                                      @RequestParam double price,
                                      @RequestParam String imageUrl) {
        productService.createProduct(name, description, price, imageUrl);
        return "redirect:/";
    }

    @GetMapping("/admin-product-details")
    public String showAdminProductDetails(@RequestParam int id, Model model) {
        var product = productService.getProductById(id);
        if (product.isEmpty()) return "redirect:/";
        model.addAttribute("product", product.get());
        return "admin-product-details";
    }

    @PostMapping("/admin-product-details/update")
    public String handleUpdateProduct(@RequestParam int id,
                                      @RequestParam String name,
                                      @RequestParam String description,
                                      @RequestParam double price,
                                      @RequestParam String imageUrl) {
        productService.updateProduct(id, name, description, price, imageUrl);
        return "redirect:/admin-product-details?id=" + id;
    }

    @PostMapping("/admin-product-details/hide")
    public String handleToggleHidden(@RequestParam int id) {
        productService.toggleHidden(id);
        return "redirect:/admin-product-details?id=" + id;
    }

    @PostMapping("/admin-product-details/delete")
    public String handleDeleteProduct(@RequestParam int id) {
        productService.deleteProduct(id);
        return "redirect:/";
    }
}
