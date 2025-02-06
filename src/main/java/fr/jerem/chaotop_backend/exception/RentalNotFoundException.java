package fr.jerem.chaotop_backend.exception;

import lombok.Getter;

@Getter
public class RentalNotFoundException extends RuntimeException {
    private String source;

    public RentalNotFoundException(String message, String source) {
        super(message);
        this.source = source;
    }

    public RentalNotFoundException(String message) {
        super(message);
    }
}
