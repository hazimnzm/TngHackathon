package Hackathing.BackendTemplate.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) //required for REST APIs
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //no cookies, use JWT
                .authorizeHttpRequests(auth -> auth
                    // public auth endpoints (login signup)
                        .requestMatchers("/auth/**").permitAll()
                        // Swagger/OpenAPI Documentation (Public)
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        // Merchant Management (LOCKED) .authenticated()
                        .requestMatchers(HttpMethod.GET,"/inventory/**").authenticated()
                        .requestMatchers("/item/**").authenticated()
                        .requestMatchers("/my/items/**").permitAll()
                        // Test Endpoints (Optional: Public)
                        .requestMatchers("/test/**").authenticated()
                        .requestMatchers(HttpMethod.POST).permitAll()
                        //default fallback
                        .anyRequest().permitAll()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}

