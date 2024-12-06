package us.cloud.teachme.auth_service.model;

import java.beans.Transient;
import java.util.Collection;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Document(collection = "User")
public class User {
  
  @Id
  private String id;

  @Indexed(unique = true)
  @Schema(description = "Email of the user", example = "john.doe@gmail.com")
  private String email;

  @Schema(description = "Password of the user", example = "P@ssw0rd")
  private String password;

  @Schema(description = "Role of the user", example = "USER | ADMIN")
  private String role;

  @JsonIgnore
  public String getPassword() {
    return password;
  }

  @Transient
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role));
  }

  @Schema(name = "enabled", description = "Is the user account enabled", example = "true")
  public boolean isEnabled() {
    return true;
  }

  

}
