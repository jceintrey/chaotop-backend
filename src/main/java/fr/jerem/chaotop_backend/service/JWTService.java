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
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.jwk.source.ImmutableSecret;

@Service
public class JWTService {

    private final String jwtKey;
    private final JwtDecoder jwtDecoder;
    private final JwtEncoder jwtEncoder;

    public JWTService(@Value("${jwt.secret.key}") String jwtKey) {
        this.jwtKey = jwtKey;

        SecretKeySpec secretKey = new SecretKeySpec(getByteKey(), "HmacSHA256");
        this.jwtDecoder = NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS256).build();

        this.jwtEncoder = new NimbusJwtEncoder(new ImmutableSecret<>(getByteKey()));
    }

    private static final Logger logger = LoggerFactory.getLogger(JWTService.class);

    public String generateToken(Authentication authentication) {
        logger.info("generate Token for authentication :" + authentication.toString());
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.MINUTES))
                .subject(authentication.getName())
                .claim("roles", "USER")
                .build();

        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters
                .from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);

        return jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
    }

    public JwtDecoder getJwtDecoder() {

        return this.jwtDecoder;

    }

    public JwtEncoder getJwtEncoder() {

        return this.jwtEncoder;

    }

    public byte[] getByteKey() {
        return this.jwtKey.getBytes();
    }
}
