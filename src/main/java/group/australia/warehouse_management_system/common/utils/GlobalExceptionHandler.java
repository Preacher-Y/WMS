package group.australia.warehouse_management_system.common.utils;

import group.australia.warehouse_management_system.common.dtos.ErrorDto;
import group.australia.warehouse_management_system.common.exceptions.BadRequestException;
import group.australia.warehouse_management_system.common.exceptions.ConflictException;
import group.australia.warehouse_management_system.common.exceptions.NotFoundException;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.exc.InvalidFormatException;

import java.util.Objects;
import java.util.stream.Collectors;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDto> handleNotFoundException(
            NotFoundException exception
    ) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorDto(exception.getMessage()));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorDto> handleConflictException(
            ConflictException exception
    ) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorDto(exception.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    public  ResponseEntity<ErrorDto> handleBadRequestException(
            BadRequestException ex
    ) {
        return  ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(
            MethodArgumentNotValidException exception
    ) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDto> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException exception
    ) {
        Throwable cause = exception.getCause();
        String message;

        if (cause instanceof InvalidFormatException ife) {
            String field = ife.getPath().stream()
                    .map(JacksonException.Reference::getPropertyName)
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining("."));

            String expectedType = getExpectedType(ife);
            String receivedValue = String.valueOf(ife.getValue());

            message = String.format(
                    "Field '%s' expects a '%s' value but received '%s'.",
                    field.isEmpty() ? "unknown" : field,
                    expectedType,
                    receivedValue
            );
        } else {
            message = "Malformed or unreadable JSON in request body.";
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDto(message));
    }

    private @NonNull String getExpectedType(InvalidFormatException ife) {
        String expectedType;

        if (ife.getTargetType().getSimpleName().contains("Integer")){
            expectedType = "Number";
        } else if (ife.getTargetType().getSimpleName().contains("Boolean")) {
            expectedType = "True or False";
        } else if (ife.getTargetType().getSimpleName().contains("Float")) {
            expectedType = "Decimal Number";
        } else {
            expectedType = ife.getTargetType().getSimpleName();
        }
        return expectedType;
    }
}
