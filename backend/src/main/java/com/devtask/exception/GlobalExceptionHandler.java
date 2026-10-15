package com.devtask.exception;

import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<?> runtime(RuntimeException e){
        HttpStatus status = e instanceof IllegalArgumentException ? HttpStatus.CONFLICT : HttpStatus.INTERNAL_SERVER_ERROR;
        String message = e instanceof IllegalArgumentException ? e.getMessage() : "Unexpected server error";
        return ResponseEntity.status(status).body(Map.of("error",message));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<?> notFound(ResourceNotFoundException e){return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error",e.getMessage()));}

    @ExceptionHandler(BadCredentialsException.class)
    ResponseEntity<?> badCredentials(BadCredentialsException e){return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error","Invalid email or password"));}

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<?> validation(MethodArgumentNotValidException e){
        return ResponseEntity.badRequest().body(Map.of("error","Validation failed"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<?> denied(AccessDeniedException e){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error",e.getMessage()));
    }
}
