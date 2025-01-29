package fr.jerem.chaotop_backend.dto;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class RentalCreateRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Surface is required")
    @Positive(message = "Surface must be positive")
    private double surface;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;
    @URL(message = "picture must be an URL")
    private String picture;

    private String description;

}
