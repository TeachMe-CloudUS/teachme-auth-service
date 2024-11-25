package us.cloud.teachme.auth_service.controller;

import java.security.SignatureException;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import us.cloud.teachme.auth_service.exceptions.UserNotFoundException;

@ControllerAdvice
public class AdviceController {

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(UserNotFoundException.class)
  public Map<String, String> handleNotFoundException(UserNotFoundException ex) {
    return Map.of("error", ex.getMessage());
  }

  @ResponseStatus(HttpStatus.OK)
  @ExceptionHandler(SignatureException.class)
  public String handleJwtException(SignatureException ex) {
    return "Invalid token";
  }
  
}
