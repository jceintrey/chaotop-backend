package fr.jerem.chaotop_backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalResponse {

    private Integer id;
    private String Name;
    private double surface;
    private BigDecimal price;
    private String picture;
    private String description;
    private Integer owner;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
