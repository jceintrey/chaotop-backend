package fr.jerem.chaotop_backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for a rental response.
 * <p>
 * Represents the response body returned after rental creation or retrieve.
 * </p>
 */

@Schema(description = "Response body returned after rental creation or retrieve giving the detailed informations about the rental.")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalResponse {

    @Schema(description = "Unique identifier for the rental")
    private Integer id;
    @Schema(description = "Title or name of the rental", example = "A charming tiny house")
    private String name;
    @Schema(description = "Surface of the rental")
    private double surface;
    @Schema(description = "Price of the rental")
    private BigDecimal price;
    @Schema(description = "URL link reference to the picture")
    private String picture;
    @Schema(description = "Detailed description of the rental", example = "A charming tiny house with 2 cozy bedrooms, a fully equipped kitchen, ...")
    private String description;
    @Schema(description = "Owner Id of the rental")
    @JsonProperty("owner_id")
    private Integer owner;
    @Schema(description = "Timestamp date of creation")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @Schema(description = "Timestamp date of last update")
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

}
