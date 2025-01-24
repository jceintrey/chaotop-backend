package fr.jerem.chaotop_backend.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
//@Configuration
@Validated
@ConfigurationProperties(prefix = "chaotop")
public class AppConfigProperties {
    @Pattern(regexp = "^[A-Za-z0-9@$!%*?&]{64,}$", message = "Key size must be 512 bits at least")
    @NotNull
    @NotEmpty
    private String jwtsecretkey;
    private String jwtexpirationtime;



}
