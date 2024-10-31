package io.github.burymydeadhoreses.userauthwithtests.advices;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UniversalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleDataIntegrityViolationException(final RuntimeException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}