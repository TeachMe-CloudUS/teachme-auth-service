package us.cloud.teachme.auth_service.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import us.cloud.teachme.auth_service.model.RegisterRequest;
import us.cloud.teachme.auth_service.model.SignInRequest;
import us.cloud.teachme.auth_service.model.User;
import us.cloud.teachme.auth_service.service.JwtService;
import us.cloud.teachme.auth_service.service.UserService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "API for authentication in teachme")
public class AuthController {

  private final UserService userService;
  private final JwtService jwtService;
  private final PasswordEncoder passwordEncoder;

  @PostMapping("/signin")
  @Operation(summary = "Sign in", description = "Sign in to teachme platform")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Sign in successful"),
      @ApiResponse(responseCode = "400", description = "Invalid username or password")
  })
  public ResponseEntity<?> signin(@RequestBody SignInRequest signInRequest) {
    User user = userService.findUserByEmail(signInRequest.email());
    if (!passwordEncoder.matches(signInRequest.password(), user.getPassword())) {
      return ResponseEntity.badRequest().body(Map.of("error", "Invalid username or password"));
    }
    return ResponseEntity.ok(Map.of("token", jwtService.generateToken(user)));
  }

  @PostMapping("/register")
  @Operation(summary = "Register", description = "Register to teachme platform, not implemented yet")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Register successful"),
      @ApiResponse(responseCode = "400", description = "Username already exists")
  })
  public ResponseEntity<Void> register(@RequestBody RegisterRequest registerRequest) {
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }

  @GetMapping("/validate")
  @Operation(summary = "Validate", description = "Validate the token of the request", security = {
      @SecurityRequirement(name = "bearer-key") })
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Token is valid"),
      @ApiResponse(responseCode = "400", description = "Invalid token")
  })
  public ResponseEntity<Void> validate() {
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/me")
  @Operation(summary = "Me", description = "Get the user of the request", security = {
      @SecurityRequirement(name = "bearer-key") })
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User found"),
      @ApiResponse(responseCode = "401", description = "Unauthorized")
  })
  public ResponseEntity<Object> me(@AuthenticationPrincipal User user) {
    return ResponseEntity.ok(user);
  }

  @GetMapping("/activate")
  @Operation(summary = "Activate", description = "Activate the user with the given code", parameters = {
      @Parameter(name = "code", description = "The activation code", required = true)
  })
  @ApiResponses(value = {
      @ApiResponse(responseCode = "301", description = "User activated"),
      @ApiResponse(responseCode = "404", description = "User not found")
  })
  public ResponseEntity<Object> activate(@RequestParam("code") String code) {
    userService.activateUser(code);
    return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header("Location", "/").build();
  }

}
