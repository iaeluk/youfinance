package com.iaeluk.finance.config;

import com.iaeluk.finance.model.User;
import com.iaeluk.finance.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class UserCreationFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            System.out.println(
                    jwt.getClaims().toString()
            );

            String email = jwt.getClaim("email");

            if (email != null && userService.findByEmail(email).isEmpty()) {
                String name = jwt.getClaim("name");
                String picture = jwt.getClaim("picture");
                String sub = jwt.getClaim("sub");

                User user = new User();
                user.setEmail(email);
                user.setName(name);
                user.setPicture(picture);
                user.setSub(sub);

                userService.saveUser(user);
            }
        }

        filterChain.doFilter(request, response);
    }
}