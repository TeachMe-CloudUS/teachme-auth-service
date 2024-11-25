package us.cloud.teachme.auth_service.exceptions;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException() {
    super("User not found.");
  }
  
}
