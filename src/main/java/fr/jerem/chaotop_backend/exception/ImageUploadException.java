package fr.jerem.chaotop_backend.exception;

public class ImageUploadException extends RuntimeException {

    public ImageUploadException(String message, Throwable cause) {
        super(message, cause);
    }
    public ImageUploadException(String message) {
        super(message);
    }

}