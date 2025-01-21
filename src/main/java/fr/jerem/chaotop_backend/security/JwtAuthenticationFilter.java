package fr.jerem.chaotop_backend.security;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal
    (@SuppressWarnings("null") HttpServletRequest request,
    @SuppressWarnings("null") HttpServletResponse response,
     @SuppressWarnings("null") FilterChain filterChain)
            throws ServletException, IOException {
        // This filter does nothing. It simply passes the request and response down the chain.
        filterChain.doFilter(request, response);
    }
}