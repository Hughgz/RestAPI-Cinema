package com.example.API_Cinema.config;

import com.example.API_Cinema.filter.JwtTokenFilter;
import com.example.API_Cinema.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
@EnableWebMvc
public class WebSecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(request -> {
                    request
                            //Request user
                            .requestMatchers(HttpMethod.POST, "/api/user/login").permitAll()
                            .requestMatchers(HttpMethod.POST, "/api/user/register").permitAll()
                            .requestMatchers(HttpMethod.PUT, "/api/user/**").hasAnyRole(Role.USER, Role.ADMIN)
                            .requestMatchers(HttpMethod.DELETE, "/api/user/**").hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.GET, "/api/user/**").hasAnyRole(Role.ADMIN, Role.USER)

                            //Request Movie
                            .requestMatchers(HttpMethod.POST, "/api/movie/**").hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.PUT, "/api/movie/**").hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.DELETE, "/api/movie/**").hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.GET, "/api/movie/**").permitAll()

                            //Request Branch
                            .requestMatchers(HttpMethod.POST, "/api/branch/**").hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.PUT, "/api/branch/**").hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.DELETE, "/api/branch/**").hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.GET, "/api/branch/**").permitAll()

                            //Request Schedule
                            .requestMatchers(HttpMethod.POST, "/api/schedule/**").hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.PUT, "/api/schedule/**").hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.DELETE, "/api/schedule/**").hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.GET, "/api/schedule/**").permitAll()

                            //Request BLog
                            .requestMatchers(HttpMethod.POST, "/api/blog/**").hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.PUT, "/api/blog/**").hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.DELETE, "/api/blog/**").hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.GET, "/api/blog/**").permitAll()

                            //Request Bill
                            .requestMatchers(HttpMethod.POST, "/api/bill/**").hasAnyRole(Role.ADMIN, Role.USER)
                            .requestMatchers(HttpMethod.PUT, "/api/bill/**").hasAnyRole(Role.ADMIN, Role.USER)
                            .requestMatchers(HttpMethod.DELETE, "/api/bill/**").hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.GET, "/api/bill/**").hasAnyRole(Role.ADMIN, Role.USER)

                            //Request Room
                            .requestMatchers(HttpMethod.POST, "/api/room/**").hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.PUT, "/api/room/**").hasAnyRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.DELETE, "/api/room/**").hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.GET, "/api/room/**").permitAll()

                            //Request Ticket
                            .requestMatchers(HttpMethod.GET, "/api/ticket/**").hasAnyRole(Role.USER, Role.ADMIN)
                            //Request Seat
                            .requestMatchers(HttpMethod.POST, "/api/seat/**").hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.PUT, "/api/seat/**").hasAnyRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.DELETE, "/api/seat/**").hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.GET, "/api/seat/**").permitAll()

                            //Request Food
                            .requestMatchers(HttpMethod.POST, "/api/food/**").hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.PUT, "/api/food/**").hasAnyRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.DELETE, "/api/food/**").hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.GET, "/api/food/**").permitAll()

                            .requestMatchers("/api/payment/vnpay-payment").permitAll()
                            //Request bill
                            .requestMatchers(HttpMethod.POST, "/api/bill/**").permitAll()
                            .anyRequest()
                            .authenticated();
                })
                .csrf(AbstractHttpConfigurer::disable);
        http.cors(new Customizer<CorsConfigurer<HttpSecurity>>() {
            @Override
            public void customize(CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(List.of("*"));
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token", "site-type"));
                configuration.setExposedHeaders(List.of("x-auth-token"));
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                httpSecurityCorsConfigurer.configurationSource(source);
            }
        });


        return http.build();
    }
}
