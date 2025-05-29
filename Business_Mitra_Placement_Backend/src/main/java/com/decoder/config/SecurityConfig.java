package com.decoder.config;

import com.decoder.service.CustomUserDetailsService;
import com.decoder.service.RecruiterDetailsService;

import jakarta.annotation.PostConstruct;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private RecruiterDetailsService recruiterDetailsService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @PostConstruct
    public void init() {
        System.out.println("SecurityConfig loaded ✅");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
//        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
//
//        DaoAuthenticationProvider recruiterProvider = new DaoAuthenticationProvider();
//        recruiterProvider.setUserDetailsService(recruiterDetailsService);
//        recruiterProvider.setPasswordEncoder(passwordEncoder());
//
//        DaoAuthenticationProvider userProvider = new DaoAuthenticationProvider();
//        userProvider.setUserDetailsService(customUserDetailsService);
//        userProvider.setPasswordEncoder(passwordEncoder());
//
//        authBuilder.authenticationProvider(recruiterProvider);
//        authBuilder.authenticationProvider(userProvider);
//
//        return authBuilder.build();
//    }
    
    @Bean("userAuthenticationManager")
    public AuthenticationManager userAuthenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(provider);
    }

    @Bean("recruiterAuthenticationManager")
    @Primary
    public AuthenticationManager recruiterAuthenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(recruiterDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(provider);
    }


    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(Customizer.withDefaults())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/api/recruiter/login", "/api/recruiter/signup", "/api/recruiter/check-email",
                    "/api/user/login", "/api/user/register",
                    "/api/public/**"
                ).permitAll()
                .requestMatchers(HttpMethod.GET, "/api/applications").hasRole("USER") // ✅ ADD THIS LINE
                .requestMatchers(HttpMethod.POST, "/api/applications").hasRole("USER")
                .anyRequest().authenticated()
            )
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(401);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"error\": \"Unauthorized - Invalid credentials or token\"}");
                })
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
