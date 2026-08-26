package employee_management.demo.Exception;

import employee_management.demo.DTO.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponse<Object>> handleResourceNotFound(
            ResourceNotFoundException e
    ) {

        APIResponse<Object> response = new APIResponse<>(
                e.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                "NOT_FOUND",
                null,
                null
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<APIResponse<Object>> handleInvalidEmployeeRequest(
            IllegalArgumentException e
    ) {
        APIResponse<Object> response = new APIResponse<>(
                e.getMessage(),
                HttpStatus.CONFLICT.value(),
                "CONFLICT",
                null,
                null
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse<Object>> handleValidation(
            MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        APIResponse<Object> response = new APIResponse<>(
                "Request validation failed",
                HttpStatus.BAD_REQUEST.value(),
                "VALIDATION_ERROR",
                message,
                null
        );
        return ResponseEntity.badRequest().body(response);
    }
}
