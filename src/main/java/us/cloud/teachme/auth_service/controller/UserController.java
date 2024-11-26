package us.cloud.teachme.auth_service.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import us.cloud.teachme.auth_service.model.User;
import us.cloud.teachme.auth_service.model.UserDto;
import us.cloud.teachme.auth_service.model.UserValidator;
import us.cloud.teachme.auth_service.service.UserService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

  private final UserService userService;

  private final UserValidator userValidator;

  @GetMapping
  public ResponseEntity<List<User>> findAllUsers() {
    User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if(!loggedUser.getRole().equals("ADMIN")) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    return ResponseEntity.ok(userService.findAllUsers());
  }

  @GetMapping("/{userId}")
  public ResponseEntity<User> findById(@PathVariable String userId) {
    User user = userService.findUserById(userId);
    return ResponseEntity.ok(user);
  }

  @PostMapping
  public ResponseEntity<?> createUser(@Validated @RequestBody UserDto userDto) {
    User user = User.builder().username(userDto.username()).password(userDto.password()).build();
    Errors errors = userValidator.validateObject(user);
    if(errors.hasErrors()) {
      return ResponseEntity.badRequest().body(errors.getAllErrors());
    }
    return ResponseEntity.ok(Map.of("userId", userService.createUser(user).getId()));
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<Void> deleteUser(@PathVariable String userId){
    User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if(!loggedUser.getRole().equals("ADMIN") && !loggedUser.getId().equals(userId)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    userService.deleteUser(userId);
    return ResponseEntity.ok().build();
  }
  
}
