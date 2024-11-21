package us.cloud.teachme.auth_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

  
}
