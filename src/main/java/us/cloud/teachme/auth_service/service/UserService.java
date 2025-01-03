package us.cloud.teachme.auth_service.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import us.cloud.teachme.auth_service.exceptions.UserAlreadyExistsException;
import us.cloud.teachme.auth_service.exceptions.UserNotFoundException;
import us.cloud.teachme.auth_service.model.ActivationCode;
import us.cloud.teachme.auth_service.model.User;
import us.cloud.teachme.auth_service.repository.ActivationCodeRepository;
import us.cloud.teachme.auth_service.repository.UserRepository;

@Service
@AllArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  private final MailService mailService;

  private final ActivationCodeRepository activationCodeRepository;

  public List<User> findAllUsers() {
    return userRepository.findAll();
  }

  public User findUserById(String id){
    return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
  }

  public User findUserByEmail(String email) {
    return userRepository.findByEmail(email).stream().findFirst().orElseThrow(UserNotFoundException::new);
  }

  public User createUser(User user) {
    if(!userRepository.findByEmail(user.getEmail()).isEmpty()) {
      throw new UserAlreadyExistsException();
    }
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setRole("USER");
    user.setEnabled(false);
    System.out.println("Sending email to: " + user.getEmail());

    ActivationCode code;

    Optional<ActivationCode> activationCode = activationCodeRepository.findByEmail(user.getEmail());

    code = activationCode.orElse(activationCodeRepository.save(ActivationCode.builder().email(user.getEmail()).build()));

    mailService.sendActivationMail(code);
    return userRepository.save(user);
  }

  public void deleteUser(String id) {
    findUserById(id);
    userRepository.deleteById(id);
  }

  public void activateUser(String code) {
    ActivationCode activationCode = activationCodeRepository.findById(code).orElseThrow(UserNotFoundException::new);
    User user = findUserByEmail(activationCode.getEmail());
    user.setEnabled(true);
    userRepository.save(user);
    activationCodeRepository.deleteById(code);
  }
  
}
