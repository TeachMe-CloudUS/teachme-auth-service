package us.cloud.teachme.auth_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import us.cloud.teachme.auth_service.model.User;
import us.cloud.teachme.auth_service.service.UserService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

  private final UserService userService;

  @GetMapping
  public ResponseEntity<List<User>> findAllUsers() {
    return ResponseEntity.ok(userService.findAllUsers());
  }

  @GetMapping("/{userId}")
  public ResponseEntity<User> findById(@PathVariable String userId) {
    User user = userService.findUserById(userId);
    return ResponseEntity.ok(user);
  }

  @PostMapping
  public ResponseEntity<String> createUser(@RequestBody User user) {
    return ResponseEntity.ok(userService.saveUser(user).getId());
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<Void> deleteUser(@PathVariable String userId){
    User user = userService.findUserById(userId);
    if (user != null){
      userService.deleteUser(user.getId());
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }
  
}
