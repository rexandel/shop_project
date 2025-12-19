package org.example.shop.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, List<String>>> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    Map<String, List<String>> errors = new HashMap<>();

    ex.getBindingResult()
        .getAllErrors()
        .forEach(
            (error) -> {
              FieldError fieldError = (FieldError) error;

              String fieldPath = buildFieldPath(fieldError);
              String errorMessage = error.getDefaultMessage();

              errors.computeIfAbsent(fieldPath, k -> new ArrayList<>()).add(errorMessage);
            });

    return ResponseEntity.badRequest().body(errors);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, String>> handleIllegalArgumentException(
      IllegalArgumentException ex) {
    Map<String, String> response = new HashMap<>();
    response.put("error", ex.getMessage());
    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
    Map<String, String> response = new HashMap<>();
    response.put("error", "Internal server error");
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }

  private String buildFieldPath(FieldError fieldError) {
    String fieldName = fieldError.getField();

    if (fieldName.contains("[")) {
      String[] parts = fieldName.split("\\[");
      if (parts.length > 0) {
        String baseField = parts[0];
        String capitalized =
            baseField.substring(0, 1).toUpperCase() + baseField.substring(1);
        if (parts.length > 1) {
          return capitalized
              + "["
              + String.join("[", java.util.Arrays.copyOfRange(parts, 1, parts.length));
        }
        return capitalized;
      }
    }

    if (!fieldName.isEmpty()) {
      return fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    return fieldName;
  }
}
