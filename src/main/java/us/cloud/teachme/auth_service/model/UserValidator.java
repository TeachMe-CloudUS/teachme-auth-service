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
    ValidationUtils.rejectIfEmpty(errors, "username", "username.empty", "The username can't be null");
    User user = (User) target;
    if(user.getUsername().length() < 3 || user.getUsername().length() > 20) {
      errors.rejectValue("username", "username.length", "The username length must be between 3 and 20 characters");
    }
  }
  
}
