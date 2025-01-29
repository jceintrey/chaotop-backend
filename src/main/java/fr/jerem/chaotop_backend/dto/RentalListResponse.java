package fr.jerem.chaotop_backend.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RentalListResponse {
    private List<RentalResponse> rentals;
}
