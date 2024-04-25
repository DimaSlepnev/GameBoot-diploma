package com.dmytro.gameboot.config;

import com.dmytro.gameboot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableAsync
public class WebSecurityConfig {

    private final UserService userService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeRequests()
                .requestMatchers(
                        "/game-boot/registration/**",
                        "/game-boot/login",
                        "/game-boot/main",
                        "/game-boot/game/**")
                .permitAll()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .logout()
                .logoutUrl("/logout")
                .and()
                .formLogin(form -> form.loginPage("/game-boot/login").loginProcessingUrl("/game-boot/login")
                        .defaultSuccessUrl("/game-boot/main")
                        .permitAll())
                .logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/game-boot/login").permitAll());
        return http.build();

    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
