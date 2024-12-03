package us.cloud.teachme.auth_service.model;

import java.beans.Transient;
import java.util.Collection;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Document(collection = "User")
public class User implements UserDetails {
  
  @Id
  private String id;

  @Schema(description = "Username of the user", example = "john.doe")
  private String username;
  
  @Schema(description = "Password of the user", example = "P@ssw0rd")
  private String password;

  @Schema(description = "Role of the user", example = "USER | ADMIN")
  private String role;

  @Override
  @JsonIgnore
  public String getPassword() {
    return password;
  }

  @Override
  @Transient
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role));
  }

  @Override
  @Transient
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  @Transient
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  @Transient
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  @Schema(name = "enabled", description = "Is the user account enabled", example = "true")
  public boolean isEnabled() {
    return true;
  }

  

}
