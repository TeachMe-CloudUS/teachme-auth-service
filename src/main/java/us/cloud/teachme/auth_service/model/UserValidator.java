package us.cloud.teachme.auth_service.model;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    return User.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    ValidationUtils.rejectIfEmpty(errors, "email", "email.empty", "The email can't be null");
    User user = (User) target;
    if(user.getEmail().length() <= 3 || user.getEmail().length() > 60) {
      errors.rejectValue("email", "email.length", "The email length must be between 4 and 60 characters");
    }
    if(!user.getEmail().matches("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
      errors.rejectValue("email", "email.invalid", "The email is invalid");
    }
  }
  
}
