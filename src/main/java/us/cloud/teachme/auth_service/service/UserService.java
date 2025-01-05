package us.cloud.teachme.auth_service.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import us.cloud.teachme.auth_service.exceptions.InvalidCredentialsException;
import us.cloud.teachme.auth_service.exceptions.UserAlreadyExistsException;
import us.cloud.teachme.auth_service.exceptions.UserNotFoundException;
import us.cloud.teachme.auth_service.model.ActivationCode;
import us.cloud.teachme.auth_service.model.KafkaTopics;
import us.cloud.teachme.auth_service.model.User;
import us.cloud.teachme.auth_service.repository.ActivationCodeRepository;
import us.cloud.teachme.auth_service.repository.UserRepository;
import us.cloud.teachme.auth_service.request.UpdateUserRequestDto;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  private final MailService mailService;

  private final ActivationCodeRepository activationCodeRepository;

  @Autowired(required = false)
  private KafkaTemplate<String, Object> kafkaTemplate;

  public List<User> findAllUsers() {
    return userRepository.findAll();
  }

  public User findUserById(String id) {
    return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
  }

  public User findUserByEmail(String email) {
    return userRepository.findByEmail(email).stream().findFirst().orElseThrow(UserNotFoundException::new);
  }

  public User createUser(User user) {
    if (!userRepository.findByEmail(user.getEmail()).isEmpty()) {
      throw new UserAlreadyExistsException();
    }
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setRole("USER");
    user.setEnabled(false);
    System.out.println("Sending email to: " + user.getEmail());

    ActivationCode code;

    Optional<ActivationCode> activationCode = activationCodeRepository.findByEmail(user.getEmail());

    code = activationCode
        .orElse(activationCodeRepository.save(ActivationCode.builder().email(user.getEmail()).build()));

    User savedUser = userRepository.save(user);
    mailService.sendActivationMail(savedUser.getId(), code);
    if (kafkaTemplate != null)
      kafkaTemplate.send(KafkaTopics.USER_CREATED.getTopic(), savedUser);
    return savedUser;
  }

  public User updateUser(String userId, UpdateUserRequestDto updateUserRequestDto) {
    User user = findUserById(userId);
    if (!passwordEncoder.matches(updateUserRequestDto.oldPassword(), user.getPassword()))
      throw new InvalidCredentialsException();
    user.setPassword(passwordEncoder.encode(updateUserRequestDto.newPassword()));
    User updatedUser = userRepository.save(user);
    if (kafkaTemplate != null)
      kafkaTemplate.send(KafkaTopics.USER_UPDATED.getTopic(), updatedUser);
    return updatedUser;
  }

  public void deleteUser(String id) {
    User user = findUserById(id);
    userRepository.deleteById(id);
    if (kafkaTemplate != null)
      kafkaTemplate.send(KafkaTopics.USER_DELETED.getTopic(), user);
  }

  public void activateUser(String code) {
    ActivationCode activationCode = activationCodeRepository.findById(code).orElseThrow(UserNotFoundException::new);
    User user = findUserByEmail(activationCode.getEmail());
    user.setEnabled(true);
    user = userRepository.save(user);
    activationCodeRepository.deleteById(code);
    if (kafkaTemplate != null)
      kafkaTemplate.send(KafkaTopics.USER_ACTIVATED.getTopic(), user);
  }

}
