package com.iaeluk.finance.service;

import com.iaeluk.finance.model.User;
import com.iaeluk.finance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public void findOrCreateUser(OAuth2User oAuth2User) {
        String name = oAuth2User.getAttribute("name");
        String sub = oAuth2User.getAttribute("sub");
        String email = oAuth2User.getAttribute("email");
        String picture = oAuth2User.getAttribute("picture");

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
