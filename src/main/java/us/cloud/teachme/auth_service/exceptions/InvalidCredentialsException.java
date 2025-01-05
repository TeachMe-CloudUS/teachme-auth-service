package us.cloud.teachme.auth_service.exceptions;

public class InvalidCredentialsException extends RuntimeException {

  public InvalidCredentialsException() {
    super("Invalid username or password.");
  }

}
