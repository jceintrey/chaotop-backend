package fr.jerem.chaotop_backend.configuration;

import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

/**
 * Configuration class for openAPI Swagger implementation.
 * This class defines general informations and a security scheme for Json Web
 * Token Authentication.
 */
@Configuration
@SecurityScheme(name = "Bearer_Authentication", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
@OpenAPIDefinition(info = @Info(title = "ChâTop API", version = "1.0", contact = @Contact(name = "Jérémie Ceintrey", url = "https://github.com/jceintrey")), servers = @Server(url = "http://localhost:8080/", description = "Dev Server"))
public class OpenAPIConfig {
}