package com.iaeluk.finance.config;

import com.iaeluk.finance.model.User;
import com.iaeluk.finance.repository.UserRepository;
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
import java.util.Optional;

@Component
public class UserCreationFilter extends OncePerRequestFilter {

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        // Recupera a autenticação do SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Verifica se a autenticação está presente e é uma instância de Jwt
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            System.out.println(
                    jwt.getClaims().toString()
            );

            // Recupera o email do token JWT
            String email = jwt.getClaim("email");

            // Se o email estiver presente e o usuário não estiver no banco, cria um novo
            if (email != null && userRepository.findByEmail(email).isEmpty()) {
                // Recupera informações adicionais do token
                String name = jwt.getClaim("name");
                String picture = jwt.getClaim("picture");
                String sub = jwt.getClaim("sub");

                // Cria e salva o novo usuário no banco de dados
                User user = new User();
                user.setEmail(email);
                user.setName(name);
                user.setPicture(picture);
                user.setSub(sub);

                userRepository.save(user);
            }
        }

        // Continua o fluxo da requisição
        filterChain.doFilter(request, response);
    }
}