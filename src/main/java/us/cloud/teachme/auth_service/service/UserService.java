package us.cloud.teachme.auth_service.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import us.cloud.teachme.auth_service.model.User;
import us.cloud.teachme.auth_service.repository.UserRepository;

@Service
@AllArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  public List<User> findAllUsers() {
    return userRepository.findAll();
  }

  public User findUserById(String id){
    return userRepository.findById(id).orElseThrow();
  }

  public User saveUser(User user) {
    if(user.getId() == null) {
      user.setPassword(passwordEncoder.encode(user.getPassword()));
    } else {
      user.setPassword(findUserById(user.getId()).getPassword());
    }
    return userRepository.save(user);
  }

  public void deleteUser(String id) {
    userRepository.deleteById(id);
  }
  
}
