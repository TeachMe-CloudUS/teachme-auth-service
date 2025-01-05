package us.cloud.teachme.auth_service.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateUserRequestDto(
                @Schema(description = "New password of the user.", example = "newP@ssw0rd") String newPassword,
                @Schema(description = "Old password of the user.", example = "P@ssw0rd") String oldPassword) {

}
