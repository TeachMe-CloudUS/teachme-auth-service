package us.cloud.teachme.auth_service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Document(collection = "ActivationCode")
public class ActivationCode {
  
  @Id
  private String id;

  @Indexed(unique = true)
  private String email;
  
}
