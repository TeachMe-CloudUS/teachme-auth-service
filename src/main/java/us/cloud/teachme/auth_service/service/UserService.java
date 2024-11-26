package us.cloud.teachme.auth_service.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import us.cloud.teachme.auth_service.exceptions.UserAlreadyExistsException;
import us.cloud.teachme.auth_service.exceptions.UserNotFoundException;
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
    return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
  }

  public User findUserByUsername(String username) {
    return userRepository.findByUsername(username).stream().findFirst().orElseThrow(UserNotFoundException::new);
  }

  public User createUser(User user) {
    if(!userRepository.findByUsername(user.getUsername()).isEmpty()) {
      throw new UserAlreadyExistsException();
    }
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setRole("USER");
    return userRepository.save(user);
  }

  public void deleteUser(String id) {
    findUserById(id);
    userRepository.deleteById(id);
  }
  
}
