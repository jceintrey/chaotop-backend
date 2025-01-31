package fr.jerem.chaotop_backend.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO for a getAll rental response which is a {@link List} of {@link RentalResponse}
 * <p>
 * Represents the response body returned after getAllRentals request
 * </p>
 */
@Data
@AllArgsConstructor
public class RentalListResponse {
    private List<RentalResponse> rentals;
}
