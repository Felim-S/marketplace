package com.example.marketplace.controller;

import com.example.marketplace.model.BasketItem;
import com.example.marketplace.model.Order;
import com.example.marketplace.model.Product;
import com.example.marketplace.model.User;
import com.example.marketplace.service.BasketService;
import com.example.marketplace.service.OrderService;
import com.example.marketplace.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class OrderController {

    private final BasketService basketService;
    private final ProductService productService;
    private final OrderService orderService;

    public OrderController(BasketService basketService, ProductService productService,  OrderService orderService) {
        this.basketService = basketService;
        this.productService = productService;
        this.orderService = orderService;
    }

    @GetMapping("/shopping-basket")
    public String showShoppingBasket(HttpSession httpSession, Model model) {

        User user = (User) httpSession.getAttribute("user");
        List<BasketItem> items = basketService.getBasketItemsForUser(user);
        model.addAttribute("basketItems", items);
        model.addAttribute("total", items.stream()
                .mapToDouble(basketItem -> basketItem.getProduct().getPrice() * basketItem.getQuantity())
                .sum());

        return "shopping-basket";
    }

    @GetMapping("/order-history")
    public String showOrderHistory(HttpSession httpSession, Model model) {

        User user = (User) httpSession.getAttribute("user");
        List<Order> orders = orderService.getOrdersForUser(user);
        model.addAttribute("orders", orders);

        return "order-history";
    }

    @GetMapping("/admin-order-history")
    public String showAdminOrderHistory(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "admin-order-history";
    }

    @PostMapping("/admin-order-history/update-status")
    public String handleUpdateOrderStatus(@RequestParam int orderId, @RequestParam String status) {
        orderService.updateOrderStatus(orderId, status);
        return "redirect:/admin-order-history";
    }

    @PostMapping("/basket/add")
    public String handleAddToBasket(@RequestParam int productId, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        productService.getProductById(productId).ifPresent(product ->
            basketService.addBasketItem(user, product)
        );
        return "redirect:/";
    }

    @PostMapping("/basket/update")
    public String handleUpdateBasket(@RequestParam int productId, @RequestParam int delta, HttpSession httpSession) {
        User user =  (User) httpSession.getAttribute("user");
        Optional<Product> product = productService.getProductById(productId);

        product.ifPresent(value -> basketService.updateQuantity(user, value, delta));

        return "redirect:/shopping-basket";
    }

    @PostMapping("/basket/remove")
    public String handleRemoveFromBasket(@RequestParam int productId, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        productService.getProductById(productId).ifPresent(product ->
            basketService.removeItem(user, product)
        );
        return "redirect:/shopping-basket";
    }

    @PostMapping("/basket/order")
    public String handleOrder(HttpSession httpSession) {
        User user =  (User) httpSession.getAttribute("user");

        List<BasketItem> items = basketService.getBasketItemsForUser(user);

        if (items.isEmpty()) {
            return "redirect:/shopping-basket";
        }

        orderService.makeOrder(user.getId(), items);

        basketService.clearBasket(user);

        return "redirect:/";
    }
}
