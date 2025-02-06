package fr.jerem.chaotop_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiErrorResponse {
    private String message;
    private String source;

    public ApiErrorResponse(String message){
        this.message=message;
    }
    

}
