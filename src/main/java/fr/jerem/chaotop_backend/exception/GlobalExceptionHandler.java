package fr.jerem.chaotop_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import fr.jerem.chaotop_backend.dto.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * Global Exception Handler that listen for exceptions thrown from the application
 * It returns ResponseEntity in a standardized response format.
 * 
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        log.info("ex to string<" + ex.toString() + ">");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiErrorResponse(ex.getMessage(), ex.getSource()));
    }

    @ExceptionHandler(InvalidUserDetailsException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidUserDetailsException(InvalidUserDetailsException ex) {
        log.error(ex.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(EmailAlreadyUsedException.class)
    public ResponseEntity<ApiErrorResponse> handleEmailAlreadyUsedException(EmailAlreadyUsedException ex) {
        log.error(ex.toString());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(ImageUploadException.class)
    public ResponseEntity<ApiErrorResponse> handleImageUploadException(ImageUploadException ex) {
        log.error(ex.toString());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(RentalNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleRentalNotFoundException(RentalNotFoundException ex) {
        log.error(ex.toString());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(InvalidUserProfileException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidUserProfileException(InvalidUserProfileException ex) {
        log.error(ex.toString());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(AuthenticatedUserNotFound.class)
    public ResponseEntity<ApiErrorResponse> handleAuthenticatedUserNotFound(AuthenticatedUserNotFound ex) {
        log.error(ex.toString());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("a Runtime exception has occured");
    }
}