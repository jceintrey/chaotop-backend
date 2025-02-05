package fr.jerem.chaotop_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO that represents a Token response
 * 
 * This class is used to return the Json Web Token
 * 
 */
@Schema(description = "Response body containing the JSON Web Token used for API authentication.")
@Data
@AllArgsConstructor
public class TokenResponse {
    @Schema(description = "The JSON Web Token used for authentication.", example = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJzZWxmIiwic3ViIjoiYm9iQG1haWwudGxkIiwiZXhwIjoxNzM4MzQzMTk1LCJpYXQiOjE3MzgzMzk1OTUsInJvbGVzIjoiVVNFUiJ9.dZccscSkZYawnt40cVRFF-3ds5BO7p5yhMH_Syuofrs")
    private String token;
}