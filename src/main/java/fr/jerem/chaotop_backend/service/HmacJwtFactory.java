package fr.jerem.chaotop_backend.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@Data
public class HmacJwtFactory implements JwtFactory {

    private final SecretKey secretKey;
    private JwtDecoder jwtDecoder;
    private JwtEncoder jwtEncoder;

    // Constructor with secret key
    public HmacJwtFactory(String secret) {
        if (secret == null || secret.length() < 32) { // Minimum 256 bits pour HMAC
            throw new IllegalArgumentException("Secret key must be at least 32 characters long");
        }
        this.secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
    }

    @Override
    public JwtDecoder createJwtDecoder() {
        if (this.jwtDecoder == null) {
            try {
                this.jwtDecoder = NimbusJwtDecoder
                        .withSecretKey(secretKey)
                        .macAlgorithm(MacAlgorithm.HS256)
                        .build();

                log.trace("JwtDecoder successfully created");
            } catch (Exception e) {
                log.error("Failed to create JwtDecoder: {}", e.getMessage(), e);
                throw new IllegalStateException("Unable to create JwtDecoder", e);
            }
        }
        return this.jwtDecoder;

    }

    @Override
    public JwtEncoder createJwtEncoder() {
        if (this.jwtEncoder == null) {
            try {
                JWKSource<SecurityContext> jwkSource = new ImmutableSecret<>(secretKey);
                this.jwtEncoder = new NimbusJwtEncoder(jwkSource);
                log.trace("JwtEncoder successfully created");
            } catch (Exception e) {
                log.error("Failed to create JwtEncoder: {}", e.getMessage(), e);
                throw new IllegalStateException("Unable to create JwtEncoder", e);
            }
        }
        return this.jwtEncoder;
    }

    /**
     * Creates a signed JWT token for the given authenticated user.
     *
     * @param authentication the {@link Authentication} object
     * @return a {@link String} signed JWT token.
     * @throws Exception if an error occurs
     */
    @Override
    public String createToken(Authentication authentication) throws Exception {
        log.debug("Generating token for user: {}", authentication.getName());
        // Build the claimset
        JwtClaimsSet claims = buildClaims(authentication);

        try {
            // Encode the claims in a JWT Token
            String token = encode(claims);
            log.debug("Token successfully generated for {} : {}", authentication.getName(), token);
            return token;
        } catch (Exception e) {
            log.error("Error generating token for user: {}", authentication.getName(), e);
            throw e;
        }
    }

    /**
     * Builds a {@link JwtClaimsSet} object containing the claims of the JWT token.
     *
     * @param authentication the {@link Authentication} object
     * @return a {@link JwtClaimsSet} object
     */
    private JwtClaimsSet buildClaims(Authentication authentication) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(authentication.getName())
                .claim("roles", "USER")
                .build();
        return claims;
    }

    @SuppressWarnings("unused")
    private Jwt decode(String token) {
        log.debug("Decoding token: {}", token);
        try {
            Jwt jwt = getJwtDecoder().decode(token);
            log.debug("Token successfully decoded.");
            return jwt;
        } catch (Exception e) {
            log.error("Failed to decode token.", e);
            throw e;
        }
    }

    /**
     * Encodes the given {@link JwtClaimsSet} into a JWT token string.
     *
     * @param claims the {@link JwtClaimsSet}
     * @return the {@link String} encoded JWT
     * @throws RuntimeException if an error occurs during the encoding process.
     */
    private String encode(JwtClaimsSet claims) {
        log.debug("Encoding JWT with claims: {}");
        try {
            JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters
                    .from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);
            String token = this.getJwtEncoder().encode(jwtEncoderParameters).getTokenValue();
            log.debug("Token successfully encoded.");
            return token;
        } catch (Exception e) {
            log.error("Error encoding JWT.", e);
            throw e;
        }
    }

}
