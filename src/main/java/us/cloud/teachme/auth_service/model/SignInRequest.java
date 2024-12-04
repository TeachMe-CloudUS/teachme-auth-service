package us.cloud.teachme.auth_service.model;

import io.swagger.v3.oas.annotations.media.Schema;

public record SignInRequest(@Schema(description = "Email of the user account", example = "john.doe@gmail.com") String email, @Schema(description = "Password of the user account", example = "P@ssw0rd") String password) {
  
}
