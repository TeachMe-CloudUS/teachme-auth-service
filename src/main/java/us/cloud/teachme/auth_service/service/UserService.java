package us.cloud.teachme.auth_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import us.cloud.teachme.auth_service.model.User;
import us.cloud.teachme.auth_service.repository.UserRepository;

@Service
@AllArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public List<User> findAllUsers() {
    return userRepository.findAll();
  }
  
}
