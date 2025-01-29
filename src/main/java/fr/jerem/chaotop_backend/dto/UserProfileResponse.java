package fr.jerem.chaotop_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {
    private String name;
    private String email;
    private String created_at;
    private String updated_at;
}
