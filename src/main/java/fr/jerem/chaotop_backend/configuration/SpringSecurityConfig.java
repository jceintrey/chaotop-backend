package fr.jerem.chaotop_backend.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

import fr.jerem.chaotop_backend.service.CustomUserDetailsService;
import fr.jerem.chaotop_backend.service.JwtFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * Configuration class for Spring Security settings.
 * <p>
 * This class provides the necessary beans and configurations to secure the
 * application using Spring Security.
 * It defines the security filter chain, password encoding mechanism, and
 * authentication manager setup.
 * </p>
 * <p>
 * - {@link SecurityFilterChain} configures the access control and login/logout
 * functionality.
 * - {@link PasswordEncoder} defines the password encoding mechanism, using
 * BCrypt hashing.
 * - {@link AuthenticationManager} sets up the authentication manager,
 * integrating with the custom user details service.
 * </p>
 * 
 */
@Configuration
@EnableWebSecurity
@Slf4j
public class SpringSecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private static final String[] AUTH_WHITELIST = {
            "/api/auth/login",
            "/api/auth/register",
            "/register"
    };

    private final JwtFactory jwtFactory;

    public SpringSecurityConfig(
            CustomUserDetailsService customUserDetailsService, JwtFactory jwtFactory) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtFactory = jwtFactory;

    }

    /**
     * Configures the {@link SecurityFilterChain} bean, overiding default security
     * rules applied to HTTP requests.
     * <p>
     * This configuration:
     * - Disables CSRF protection (may need to be adjusted for specific use cases).
     * - Permits access to the root URL ("/") without authentication.
     * - Requires authentication for all other requests.
     * - Configures form-based login and logout with default settings.
     * </p>
     * 
     * @param http the {@link HttpSecurity} instance used to configure HTTP security
     * @return the configured {@link SecurityFilterChain} bean
     * @throws Exception if there is a configuration error
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .anyRequest().authenticated())
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(withDefaults()))
                .build();
    }

    /**
     * Configures the {@link PasswordEncoder} bean to use BCrypt for password
     * encoding.
     * <p>
     * BCrypt is a secure hashing algorithm used to protect user passwords in the
     * database.
     * </p>
     * 
     * @return the {@link PasswordEncoder} bean configured with BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the Spring Security {@link AuthenticationManager} bean, which is
     * responsible for
     * authenticating users.
     * <p>
     * This method integrates with {@link CustomUserDetailsService} to load user
     * details and the {@link PasswordEncoder}
     * to verify user credentials during authentication.
     * </p>
     * 
     * @param http            the {@link HttpSecurity} instance, required for
     *                        configuring authentication
     * @param passwordEncoder the {@link PasswordEncoder} used to check passwords
     * @return the configured {@link AuthenticationManager} bean
     * @throws Exception if there is a configuration error
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder)
            throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http
                .getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public JwtDecoder JwtDecoder() {
        return this.jwtFactory.createJwtDecoder();
    }

    @Bean
    public JwtEncoder JwtEncoder() {
        return this.jwtFactory.createJwtEncoder();
    }

    @Bean
    public JwtFactory JwtFactory() {
        return this.jwtFactory;
    }

}
