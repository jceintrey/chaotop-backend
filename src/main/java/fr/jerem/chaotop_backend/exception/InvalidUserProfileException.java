package fr.jerem.chaotop_backend.exception;

import lombok.Getter;

@Getter
public class InvalidUserProfileException extends RuntimeException {
    private String source;

    public InvalidUserProfileException(String message, String source) {
        super(message);
        this.source = source;
    }

    public InvalidUserProfileException(String message) {
        super(message);
    }
}
