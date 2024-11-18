package org.example.expert.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String url = request.getRequestURI();
        if (url.startsWith("/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = jwtUtil.substringToken(request.getHeader("Authorization"));


        if (token != null && jwtUtil.validateToken(token)) {

            String userId = jwtUtil.extractClaims(token).getSubject();
            String email = jwtUtil.extractClaims(token).get("email", String.class);
            String userRole = jwtUtil.extractClaims(token).get("userRole", String.class);
            String nickname = jwtUtil.extractClaims(token).get("nickname", String.class);

            JwtAuthenticationToken authentication = new JwtAuthenticationToken(
                    userId, email, userRole, nickname);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
