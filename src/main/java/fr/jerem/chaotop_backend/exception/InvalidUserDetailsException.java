package fr.jerem.chaotop_backend.exception;

import lombok.Getter;

@Getter
public class InvalidUserDetailsException extends RuntimeException {
    private String source;

    public InvalidUserDetailsException(String message, String source) {
        super(message);
        this.source=source;
    }
    public InvalidUserDetailsException(String message) {
        super(message);
    }
}