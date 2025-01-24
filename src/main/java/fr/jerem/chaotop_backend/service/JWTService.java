package fr.jerem.chaotop_backend.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import javax.crypto.spec.SecretKeySpec;

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

import fr.jerem.chaotop_backend.configuration.properties.AppConfigProperties;
import lombok.extern.slf4j.Slf4j;

/**
 * JWTService class used to manage JWT operations
 * 
 * 
 * 
 */
@Service
@Slf4j
public class JWTService {

    private final JwtDecoder jwtDecoder;
    private final JwtEncoder jwtEncoder;
    private AppConfigProperties configProperties;

    public JWTService(AppConfigProperties configProperties) {
        this.configProperties = configProperties;

        //Création du Decoder
        SecretKeySpec secretKey = new SecretKeySpec(getByteKey(), "HmacSHA256");
        this.jwtDecoder = NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS256).build();
        log.trace("jwtEncoder created: {}", this.jwtDecoder.toString());

        //Création de l'encoder
        this.jwtEncoder = new NimbusJwtEncoder(new ImmutableSecret<>(getByteKey()));
        log.trace("jwtEncoder created: {}", this.jwtEncoder.toString());
        log.trace("JWTService constructor called. Secret key length: {}", this.jwtEncoder.toString());
        log.debug("JWTService initialized with secret key.");
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
        log.debug("Generating token for user: {}", authentication.getName());
        log.trace("", authentication);

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
            log.debug("Token successfully generated for {} : {}", authentication.getName(), token);
            return token;
        } catch (Exception e) {
            log.error("Error generating token for user: {}", authentication.getName(), e);
            throw e;
        }
    }

    public Jwt decode(String token) {
        log.debug("Decoding token: {}", token);
        try {
            Jwt jwt = this.getJwtDecoder().decode(token);
            log.debug("Token successfully decoded.");
            return jwt;
        } catch (Exception e) {
            log.error("Failed to decode token.", e);
            throw e;
        }
    }

    public String encode(JwtEncoderParameters jwtEncoderParameters) {
        log.debug("Encoding JWT with claims: {}", jwtEncoderParameters.getClaims());
        try {
            String token = this.getJwtEncoder().encode(jwtEncoderParameters).getTokenValue();
            log.debug("Token successfully encoded.");
            return token;
        } catch (Exception e) {
            log.error("Error encoding JWT.", e);
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
        return this.configProperties.getJwtsecretkey().getBytes();
    }
}
