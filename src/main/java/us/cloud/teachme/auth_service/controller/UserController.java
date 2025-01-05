package us.cloud.teachme.auth_service.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import us.cloud.teachme.auth_service.model.User;
import us.cloud.teachme.auth_service.model.UserValidator;
import us.cloud.teachme.auth_service.request.SignInRequest;
import us.cloud.teachme.auth_service.request.UpdateUserRequestDto;
import us.cloud.teachme.auth_service.service.UserService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "User Management", description = "API for managing users in teachme")
public class UserController {

  private final UserService userService;

  private final UserValidator userValidator;

  @GetMapping
  @Operation(summary = "Get all users", description = "Get all users from teachme platform", security = {
      @SecurityRequirement(name = "bearer-key") })
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "List of users"),
      @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content())
  })
  public ResponseEntity<List<User>> findAllUsers(@AuthenticationPrincipal User user) {
    if (user == null || !user.getRole().equals("ADMIN"))
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    return ResponseEntity.ok(userService.findAllUsers());
  }

  @GetMapping("/{userId}")
  @Operation(summary = "Get user by id", description = "Get user by id from teachme platform", security = {
      @SecurityRequirement(name = "bearer-key") })
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User found"),
      @ApiResponse(responseCode = "404", description = "User not found")
  })
  public ResponseEntity<User> findById(@PathVariable String userId) {
    User user = userService.findUserById(userId);
    return ResponseEntity.ok(user);
  }

  @PostMapping
  @Operation(summary = "Create user", description = "Create user in teachme platform")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User created"),
      @ApiResponse(responseCode = "409", description = "User already exists")
  })
  public ResponseEntity<?> createUser(@Validated @RequestBody SignInRequest signInRequest) {
    User user = User.builder().email(signInRequest.email()).password(signInRequest.password()).build();
    Errors errors = userValidator.validateObject(user);
    if (errors.hasErrors()) {
      return ResponseEntity.badRequest().body(errors.getAllErrors());
    }
    return ResponseEntity.ok(Map.of("userId", userService.createUser(user).getId()));
  }

  @PutMapping("/{userId}")
  @Operation(summary = "Update user", description = "Update user password in teachme platform", security = {
      @SecurityRequirement(name = "bearer-key") })
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User updated"),
      @ApiResponse(responseCode = "404", description = "User not found"),
      @ApiResponse(responseCode = "401", description = "Unauthorized")
  })
  public ResponseEntity<User> updateUser(@PathVariable String userId,
      @RequestBody UpdateUserRequestDto updateUserRequestDto,
      @AuthenticationPrincipal User authUser) {
    if (authUser == null || (!authUser.getRole().equals("ADMIN") && !authUser.getId().equals(userId)))
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    return ResponseEntity.ok(userService.updateUser(userId, updateUserRequestDto));
  }

  @DeleteMapping("/{userId}")
  @Operation(summary = "Delete user", description = "Delete user from teachme platform", security = {
      @SecurityRequirement(name = "bearer-key") })
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User deleted"),
      @ApiResponse(responseCode = "404", description = "User not found"),
      @ApiResponse(responseCode = "401", description = "Unauthorized")
  })
  public ResponseEntity<Void> deleteUser(@PathVariable String userId, @AuthenticationPrincipal User user) {
    if (user == null || (!user.getRole().equals("ADMIN") && !user.getId().equals(userId)))
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    userService.deleteUser(userId);
    return ResponseEntity.ok().build();
  }

}
