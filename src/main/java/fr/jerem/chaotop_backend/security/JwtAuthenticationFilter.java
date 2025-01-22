package fr.jerem.chaotop_backend.security;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import fr.jerem.chaotop_backend.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JWTService jwtService;

    public JwtAuthenticationFilter(JWTService jwtService) {
        this.jwtService = jwtService;

    }

    @Override
    protected void doFilterInternal(
            @SuppressWarnings("null") HttpServletRequest request,
            @SuppressWarnings("null") HttpServletResponse response,
            @SuppressWarnings("null") FilterChain filterChain)
            throws ServletException, IOException {

        // Récupérer l'en-tête Authorization
        String authorizationHeader = request.getHeader("Authorization");

        // Vérifier si l'en-tête contient un token JWT valide
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            try {

                Jwt jwt = this.jwtService.decode(token);
                logger.info("Token is valid");

                // Récupérer le sujet (username)
                String username = jwt.getSubject();

                // Récupérer les métadonnées
                @SuppressWarnings("null")
                String tokenValidFrom = (jwt.getIssuedAt() != null) ? jwt.getIssuedAt().toString() : "N/A";
                @SuppressWarnings("null")
                String tokenExpireAt = (jwt.getExpiresAt() != null) ? jwt.getExpiresAt().toString() : "N/A";

                logger.info("Validité du token Depuis:{} Jusqu'a:{}", tokenValidFrom, tokenExpireAt);

                // Extraire les rôles
                String rolesClaim = jwt.getClaim("roles");
                List<GrantedAuthority> authorities;

                if (rolesClaim != null) {
                    // Convertir la chaîne de rôle en liste d'autorités
                    authorities = List.of(new SimpleGrantedAuthority(rolesClaim));
                } else {
                    logger.warn("No roles found in the token for user: {}", username);
                    authorities = List.of(new SimpleGrantedAuthority("USER")); // Rôle par défaut
                }

                UserDetails userDetails = User.builder()
                        .username(username)
                        .password("")
                        .authorities(authorities)
                        .build();

                // Créer une authentification et la définir dans le contexte de sécurité
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (JwtException ex) {
                logger.error("Invalid JWT token", ex);
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("{\"error\": \"Invalid token\"}");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}