package us.cloud.teachme.auth_service.model;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserDto(@Schema(description = "Username of the user account", example = "john.doe") String username, @Schema(description = "Password of the user account", example = "P@ssw0rd") String password) {
  
}
