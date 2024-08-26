package com.iaeluk.finance.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.Instant;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomOidcUserService customOidcUserService;

    @Value("${front-url}")
    private String frontUrl;

    @Value("${allowed-origins}")
    private String allowedOrigins;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
       return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(authorizeConfig -> {
                    authorizeConfig
                            .requestMatchers("/user/token").permitAll()
                            .anyRequest().authenticated();
                })
               .oauth2Login(oauth2 -> oauth2
                       .userInfoEndpoint(userInfo -> userInfo
                               .oidcUserService(customOidcUserService))
                       .successHandler((request, response, authentication) -> {
                           String jwtToken = null;
                           Instant expiresAt = null;

                           Object principal = authentication.getPrincipal();

                           if (principal instanceof OidcUser oidcUser) {
                               jwtToken = oidcUser.getIdToken().getTokenValue();
                               expiresAt = oidcUser.getIdToken().getExpiresAt();
                           } else if (principal instanceof Jwt jwt) {
                               jwtToken = jwt.getTokenValue();
                               expiresAt = jwt.getExpiresAt();
                           }

                           assert expiresAt != null;
                           String redirectUrl = frontUrl + "?token=" + jwtToken + "&expiresAt=" + expiresAt;
                           response.sendRedirect(redirectUrl);
                       })
               )
                .oauth2ResourceServer(rs -> rs.jwt(Customizer.withDefaults()))

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl(frontUrl)
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                )
               .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin(allowedOrigins);
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
