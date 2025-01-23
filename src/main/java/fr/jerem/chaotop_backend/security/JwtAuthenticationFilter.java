package fr.jerem.chaotop_backend.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import fr.jerem.chaotop_backend.service.CustomUserDetailsService;
import fr.jerem.chaotop_backend.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JWTService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtAuthenticationFilter(JWTService jwtService, CustomUserDetailsService customUserDetailsService) {
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;

    }

    @Override
    protected void doFilterInternal(
            @SuppressWarnings("null") HttpServletRequest request,
            @SuppressWarnings("null") HttpServletResponse response,
            @SuppressWarnings("null") FilterChain filterChain)
            throws ServletException, IOException {
        logger.debug("Executing doFilterInternal");

        // Get Authorization header
        String authorizationHeader = request.getHeader("Authorization");
        logger.trace("authorizationHeader {}", authorizationHeader);
        // Verify if header contains a valid JWT
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            logger.trace("token {}", token);
            try {

                Jwt jwt = this.jwtService.decode(token);

                // Get the username
                String username = jwt.getSubject();

                // Get user details from the database via CustomUserDetailsService
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);


                // Add authentication to Spring context
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.debug("Authentication of {} successfully added to SecurityContext.", userDetails.getUsername());

            } catch (JwtException ex) {
                logger.error("Invalid JWT token");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("{\"error\": \"Invalid token\"}");
                return;
            }

        }
        filterChain.doFilter(request, response);
    }
}