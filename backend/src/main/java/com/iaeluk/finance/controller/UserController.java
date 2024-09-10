package com.iaeluk.finance.controller;

import com.iaeluk.finance.model.User;
import com.iaeluk.finance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
//import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
//import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController()
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/public")
    public String homePage() {
        return "Hello from Spring boot app";
    }

    @GetMapping("/private")
    public String privateRoute() {
        return "Private Route";
    }

    @GetMapping()
    public User getUser(@AuthenticationPrincipal Jwt jwt) {
        // Pega o email ou sub do token JWT
        String email = jwt.getClaim("email"); // ou String sub = jwt.getClaim("sub");

        // Busque o usuário no banco de dados pelo e-mail
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public record AuthStatus(boolean loggedIn) {}

    @GetMapping("/status")
    public ResponseEntity<AuthStatus> getStatus(@AuthenticationPrincipal OAuth2User oAuth2User) {
        boolean loggedIn = oAuth2User != null;
        return ResponseEntity.ok().body(new AuthStatus(loggedIn));
    }

//    @GetMapping("/q")
//    public OAuth2AuthorizedClient getQ(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient oa) {
//        System.out.println(oa);
//        return oa;
//    }

    @GetMapping("/cookie")
    String cookie(@AuthenticationPrincipal OidcUser user) {
        return user.toString();
    }

    @GetMapping("/token")
    public ResponseEntity<Map<String, String>> token() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return createResponse(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        Object principal = authentication.getPrincipal();
        String jwtToken = null;
        Instant expiresAt = null;

        System.out.println(authentication);

        switch (principal) {
            case OidcUser oidcUser -> {
                jwtToken = oidcUser.getIdToken().getTokenValue();
                expiresAt = oidcUser.getIdToken().getExpiresAt();
            }
            case Jwt jwt -> {
                jwtToken = jwt.getTokenValue();
                expiresAt = jwt.getExpiresAt();
            }
            case null, default -> {
                return createResponse(HttpStatus.UNAUTHORIZED, "Unsupported authentication type");
            }
        }

        Map<String, String> response = new HashMap<>();
        response.put("token", jwtToken);

        if (expiresAt != null) {
            response.put("expiresAt", expiresAt.toString());
        }

        return ResponseEntity.ok(response);
    }

    private ResponseEntity<Map<String, String>> createResponse(HttpStatus status, String message) {
        Map<String, String> response = new HashMap<>();
        response.put("status", message);
        return ResponseEntity.status(status).body(response);
    }


    @GetMapping("/privada")
    String privada() {
        return "MÁ OEE";
    }

    @GetMapping("/auth/token")
    public ResponseEntity<Map<String, String>> getToken(@AuthenticationPrincipal OidcUser oidcUser) {
        Map<String, String> jwt = new HashMap<>();
        jwt.put("token", oidcUser.getIdToken().getTokenValue());
        return ResponseEntity.ok().body(jwt);
    }
}
