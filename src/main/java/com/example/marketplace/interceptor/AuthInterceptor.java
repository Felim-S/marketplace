package com.example.marketplace.interceptor;

import com.example.marketplace.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        // allow all static resources
        if (handler instanceof ResourceHttpRequestHandler) {
            return true;
        }

        String path = request.getRequestURI();

        if (path.equals("/login") || path.equals("/create-account") || path.equals("/logout")) {
            return true;
        }

        User user = (User) request.getSession().getAttribute("user");

        if (user == null) {
            response.sendRedirect("/login");
            return false;
        }

        boolean isAdminPath = path.startsWith("/admin-");

        if (user.isAdmin() && !isAdminPath && !path.equals("/")) {
            response.sendRedirect("/");
            return false;
        }

        if (!user.isAdmin() && isAdminPath) {
            response.sendRedirect("/");
            return false;
        }

        return true;
    }
}
