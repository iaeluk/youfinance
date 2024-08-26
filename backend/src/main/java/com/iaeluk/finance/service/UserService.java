package com.iaeluk.finance.service;

import com.iaeluk.finance.model.User;
import com.iaeluk.finance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void findOrCreateUser(OidcUser oidcUser) {
        String name = oidcUser.getFullName(); // Método conveniente para obter o nome completo
        String sub = oidcUser.getSubject(); // Obtém o subject (sub) do ID Token
        String email = oidcUser.getEmail(); // Obtém o email do ID Token
        String picture = oidcUser.getPicture(); // Obtém a URL da foto do ID Token

        userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setSub(sub);
                    newUser.setEmail(email);
                    newUser.setName(name);
                    newUser.setPicture(picture);
                    return userRepository.save(newUser);
                });
    }
}
