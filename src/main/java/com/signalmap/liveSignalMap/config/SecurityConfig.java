
package com.signalmap.liveSignalMap.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)

                .sessionManagement(session -> session
                        .sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth


                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/auditoriums/**",
                                "/api/bssid/**"
                        ).permitAll()


                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/heatmap/**"
                        ).permitAll()


                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/measurements/wifi",
                                "/api/measurements/mobile"
                        ).permitAll()


                        .requestMatchers(
                                "/api/auditoriums",
                                "/api/auditoriums/**",
                                "/api/bssid",
                                "/api/bssid/**"
                        ).hasRole("ADMIN")


                        .anyRequest().denyAll()
                )

                .httpBasic(basic -> {});

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public UserDetailsService userDetailsService(
            @Value("${app.security.admin.username}") String username,
            @Value("${app.security.admin.password}") String passwordHash
    ) {
        if (username.isBlank()) {
            throw new IllegalStateException(
                    "Admin username must not be blank"
            );
        }

        if (!passwordHash.matches(
                "^\\$2[aby]\\$\\d{2}\\$[./A-Za-z0-9]{53}$"
        )) {
            throw new IllegalStateException(
                    "Admin password must be a valid BCrypt hash"
            );
        }

        UserDetails admin = User.builder()
                .username(username)
                .password(passwordHash)
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin);
    }


}
