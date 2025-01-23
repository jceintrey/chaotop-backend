package fr.jerem.chaotop_backend.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.jwk.source.ImmutableSecret;

/**
 * JWTService class used to manage JWT operations
 * 
 * 
 * 
 */
@Service
public class JWTService {

    private final String jwtKey;
    private final JwtDecoder jwtDecoder;
    private final JwtEncoder jwtEncoder;
    private static final Logger logger = LoggerFactory.getLogger(JWTService.class);

    public JWTService(@Value("${jwt.secret.key}") String jwtKey) {
        this.jwtKey = jwtKey;

        SecretKeySpec secretKey = new SecretKeySpec(getByteKey(), "HmacSHA256");
        this.jwtDecoder = NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS256).build();
        logger.trace("jwtEncoder created: {}", this.jwtDecoder.toString());

        this.jwtEncoder = new NimbusJwtEncoder(new ImmutableSecret<>(getByteKey()));
        logger.trace("jwtEncoder created: {}", this.jwtEncoder.toString());
        logger.trace("JWTService constructor called. Secret key length: {}", this.jwtEncoder.toString());
        logger.debug("JWTService initialized with secret key.");
    }

    /**
     * Generate an encrypted Token
     * rules applied to HTTP requests.
     * 
     * 
     * @param authentication the {@link Authentication} object used to build the
     *                       token
     * @return the generated {@link String}
     * @throws Exception if there is an error during generation
     */
    public String generateToken(Authentication authentication) throws Exception {
        logger.debug("Generating token for user: {}", authentication.getName());
        logger.trace("", authentication);

        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(authentication.getName())
                .claim("roles", "USER")
                .build();

        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters
                .from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);

        try {
            String token = encode(jwtEncoderParameters);
            logger.debug("Token successfully generated for {} : {}", authentication.getName(), token);
            return token;
        } catch (Exception e) {
            logger.error("Error generating token for user: {}", authentication.getName(), e);
            throw e;
        }
    }

    public Jwt decode(String token) {
        logger.debug("Decoding token: {}", token);
        try {
            Jwt jwt = this.getJwtDecoder().decode(token);
            logger.debug("Token successfully decoded.");
            return jwt;
        } catch (Exception e) {
            logger.error("Failed to decode token.", e);
            throw e;
        }
    }

    public String encode(JwtEncoderParameters jwtEncoderParameters) {
        logger.debug("Encoding JWT with claims: {}", jwtEncoderParameters.getClaims());
        try {
            String token = this.getJwtEncoder().encode(jwtEncoderParameters).getTokenValue();
            logger.debug("Token successfully encoded.");
            return token;
        } catch (Exception e) {
            logger.error("Error encoding JWT.", e);
            throw e;
        }
    }

    private JwtDecoder getJwtDecoder() {
        return this.jwtDecoder;

    }

    private JwtEncoder getJwtEncoder() {
        return this.jwtEncoder;

    }

    public byte[] getByteKey() {
        return this.jwtKey.getBytes();
    }
}
