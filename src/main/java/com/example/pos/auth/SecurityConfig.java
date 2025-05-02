package com.example.pos.auth;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .antMatchers("/api/auth/**").permitAll()

                        // User Endpoint permissions
                        .antMatchers(HttpMethod.POST, "/api/users").permitAll()
                        // .antMatchers(HttpMethod.POST, "/api/users").hasRole("ADMIN")
                        .antMatchers(HttpMethod.GET, "/api/users").hasRole("ADMIN")
                        .antMatchers(HttpMethod.GET, "/api/users/**").authenticated()

                        // Product Endpoint permissions
                        .antMatchers("/api/products").hasRole("ADMIN")
                        .antMatchers("/api/products/**").hasRole("ADMIN")

                        // Purchases Endpoint permissions
                        .antMatchers(HttpMethod.GET, "/api/purchases").hasRole("ADMIN")
                        .antMatchers("/api/purchases/profit").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
