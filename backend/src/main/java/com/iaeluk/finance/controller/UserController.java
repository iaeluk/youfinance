package com.iaeluk.finance.controller;

import com.iaeluk.finance.model.User;
import com.iaeluk.finance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;


    @GetMapping()
    public User getUser(@AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getClaim("email");

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found."));
    }

    public record AuthStatus(boolean loggedIn) {}

    @GetMapping("/status")
    public ResponseEntity<AuthStatus> getStatus(@AuthenticationPrincipal Jwt jwt) {
        boolean loggedIn = jwt != null;
        return ResponseEntity.ok().body(new AuthStatus(loggedIn));
    }

}
