package com.nahid.main.config;

import com.nahid.main.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
//                .csrf(AbstractHttpConfigurer::disable)
//                .headers(headerConfigurer -> headerConfigurer
//                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))

                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/user/**").hasAnyAuthority("ROLE_USER")
                        .requestMatchers("/story/{storyId}/delete").hasAnyAuthority("ROLE_USER","ROLE_ADMIN")
                        .requestMatchers("/h2-console/*").hasAnyAuthority("ROLE_ADMIN")
                        .anyRequest().permitAll())

                .formLogin(formLoginConfigurer -> formLoginConfigurer
                        .loginPage("/login")
                        .failureUrl("/login?error=true")
                        .defaultSuccessUrl("/dashboard",true))

                .logout(logoutConfigurer -> logoutConfigurer
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true"))

                .userDetailsService(customUserDetailsService)

                .build();
    }

    //    @Bean
//    public TextEncryptor textEncryptor(
//            @Value("${encryption.password}") String password,
//            @Value("${encryption.salt}") String salt) {
//        return Encryptors.queryableText(password, salt);  // Change this line
//    }

}
