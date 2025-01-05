package us.cloud.teachme.auth_service.exceptions;

public class UserAlreadyExistsException extends RuntimeException {

  public UserAlreadyExistsException() {
    super("User already exists.");
  }

}
