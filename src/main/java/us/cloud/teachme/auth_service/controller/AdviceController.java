package us.cloud.teachme.auth_service.controller;

import java.security.SignatureException;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import io.jsonwebtoken.JwtException;
import us.cloud.teachme.auth_service.exceptions.InvalidCredentialsException;
import us.cloud.teachme.auth_service.exceptions.UserAlreadyExistsException;
import us.cloud.teachme.auth_service.exceptions.UserNotFoundException;

@ControllerAdvice
public class AdviceController {

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<?> handleNotFoundException(UserNotFoundException ex) {
    // return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(InvalidCredentialsException.class)
  public ResponseEntity<?> handleInvalidCredentialsException(InvalidCredentialsException ex) {
    return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<?> handleAlreadyExistsException(UserAlreadyExistsException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", ex.getMessage()));
  }

  @ExceptionHandler({ SignatureException.class, JwtException.class })
  public ResponseEntity<?> handleJwtException() {
    return ResponseEntity.badRequest().body(Map.of("error", "Invalid token"));
  }

}
