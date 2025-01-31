package fr.jerem.chaotop_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * DTO for a rental creation response.
 * <p>
 * Represents the response body returned after rental creation.
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentalCreateResponse {

    private String message;

}
