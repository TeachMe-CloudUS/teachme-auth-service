package us.cloud.teachme.auth_service.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import us.cloud.teachme.auth_service.model.ActivationCode;

@Repository
public interface ActivationCodeRepository extends MongoRepository<ActivationCode, String> {

  Optional<ActivationCode> findByEmail(String email);
  
}
