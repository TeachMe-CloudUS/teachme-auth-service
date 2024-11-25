package us.cloud.teachme.auth_service.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import us.cloud.teachme.auth_service.model.User;
import us.cloud.teachme.auth_service.model.UserDto;
import us.cloud.teachme.auth_service.service.JwtService;
import us.cloud.teachme.auth_service.service.UserService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

  private final UserService userService;
  private final JwtService jwtService;
  private final PasswordEncoder passwordEncoder;

  @PostMapping("/signin")
  public ResponseEntity<?> signin(@RequestBody UserDto userDto) {
    User user = userService.findUserByUsername(userDto.username());
    if(!passwordEncoder.matches(userDto.password(), user.getPassword())) {
      return ResponseEntity.badRequest().body(Map.of("error", "Invalid username or password"));
    }
    return ResponseEntity.ok(jwtService.generateToken(user.getId()));
  }

  @PostMapping("/register")
  public ResponseEntity<Void> register(@RequestBody UserDto user) {
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/validate")
  public ResponseEntity<Void> validate() {
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/me")
  public ResponseEntity<Object> me() {
    return ResponseEntity.ok(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
  }

}
