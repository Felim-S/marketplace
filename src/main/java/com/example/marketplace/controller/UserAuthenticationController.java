package com.example.marketplace.controller;

import com.example.marketplace.model.User;
import com.example.marketplace.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import java.util.Optional;

@Controller
public class UserAuthenticationController {

    private final UserService userService;

    public UserAuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam String username, @RequestParam String password,
                              HttpSession session, Model model) {
        Optional<User> user = userService.authenticate(username, password);

        if (user.isPresent()) {
            session.setAttribute("user", user.get());
            return "redirect:/";
        } else{
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }

    @PostMapping("/logout")
    public String handleLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/create-account")
    public String showCreateAccount() {
        return "create-account";
    }

    @PostMapping("/create-account")
    public String handleCreateAccount(@RequestParam String username, @RequestParam String email, @RequestParam String password,
                                      @RequestParam(defaultValue = "false") boolean admin,
                                      HttpSession session, Model model){
        Optional<User> user = userService.createUser(username, email, password, admin);
        if (user.isPresent()) {
            session.setAttribute("user", user.get());
            return "redirect:/";
        } else{
            model.addAttribute("error", "An account already exists with that username or email");
            return "create-account";
        }
    }

}
