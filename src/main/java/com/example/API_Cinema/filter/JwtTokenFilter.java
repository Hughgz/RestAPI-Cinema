package com.example.API_Cinema.filter;

import com.example.API_Cinema.utils.JWTTokenUtils;
import com.example.API_Cinema.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final JWTTokenUtils jwtTokenUtils;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        try {
            if (isBypassToken(request)) {
                filterChain.doFilter(request, response);
                return;
            }
            final String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }
            final String token = authHeader.substring(7);
            final String phoneNumber = jwtTokenUtils.extractPhone(token);
            if (phoneNumber != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User existingUser = (User) userDetailsService.loadUserByUsername(phoneNumber);
                if (jwtTokenUtils.validateToken(token, existingUser)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(existingUser, null, existingUser.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            System.out.println(e.getMessage());
        }
    }

    // JwtTokenFilter.java

    private boolean isBypassToken(@NotNull HttpServletRequest request) {
        final List<Pair<String, String>> bypassTokens = Arrays.asList(
                Pair.of("POST", "/api/user/login"),
                Pair.of("POST", "/api/user/register")
        );
        String requestURI = request.getRequestURI();
        String requestMethod = request.getMethod();

        for (Pair<String, String> bypassToken : bypassTokens) {
            String tokenMethod = bypassToken.getLeft();
            String tokenUrl = bypassToken.getRight();

            if (requestURI.startsWith(tokenUrl) && requestMethod.equals(tokenMethod)) {
                return true;
            }
        }
        return false;
    }

}
