package fr.jerem.chaotop_backend.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;

public interface JwtFactory {
    JwtDecoder createJwtDecoder();

    JwtEncoder createJwtEncoder();

    String createToken(Authentication authentication) throws Exception;

}