package us.cloud.teachme.auth_service.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import us.cloud.teachme.auth_service.model.User;


public interface UserRepository extends MongoRepository<User, String>{

  List<User> findByEmail(String email);
  
}
