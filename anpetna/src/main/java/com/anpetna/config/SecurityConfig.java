package com.anpetna.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(org.springframework.security.config.annotation.web.builders.HttpSecurity http, JwtProvider jwtProvider) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .formLogin(f -> f.disable())
                .httpBasic(b -> b.disable())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/members/login").permitAll()
                        .requestMatchers("/members/join").permitAll()
                        .requestMatchers("/", "/signup", "/api/v1/**", "/members/readOne", "/members/readAll").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }



//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//        http.csrf(csrf -> csrf.disable()) // POST 테스트 편하게
//                .formLogin(form -> form
//                        .loginPage("/members/login")
//                        .loginProcessingUrl("/members/login_process")
//                        .defaultSuccessUrl("/members")
//                        .failureUrl("/members/login.html?error=true")
//                        .permitAll()
//                )
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/", "/login", "/signup").permitAll()
//                        .requestMatchers("/api/v1/**").permitAll()   // 테스트용
//                        .requestMatchers("/members/join").permitAll() // GET/POST 모두 허용
//                        .requestMatchers("/members/modify/").permitAll()
//                        .requestMatchers("/members/delete/").permitAll()
//                        .requestMatchers("/members/readOne").permitAll()
//                        .requestMatchers("/members/readAll").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .headers(headers -> headers
//                        .frameOptions(frame -> frame.disable()) // H2 콘솔 허용
//                );
//
//        return http.build();
//    }


//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
//        http.formLogin(form -> {form
//                .loginPage("/members/login")
//                .loginProcessingUrl("/members/login_process")
//                .defaultSuccessUrl("/members")
//                .failureUrl("/members/login.html?error=true")
//                .permitAll();
//        });
//
//        http.authorizeHttpRequests((auth) -> auth
//                        .requestMatchers("/", "/login", "/signup").permitAll()
//                        .requestMatchers("/admin").hasRole("ADMIN")
//                        .requestMatchers("/api/v1/**").hasAnyRole("USER", "ADMIN")
//                        .anyRequest().authenticated()
//                );
//
//        return http.build();
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
