package us.cloud.teachme.auth_service.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import us.cloud.teachme.auth_service.model.SignInRequest;
import us.cloud.teachme.auth_service.model.User;
import us.cloud.teachme.auth_service.repository.UserRepository;
import us.cloud.teachme.auth_service.service.JwtService;
import us.cloud.teachme.auth_service.service.UserService;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class UserControllerTest {

  @MockBean
  private UserService userService;

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private JwtService jwtService;

  @MockBean
  private PasswordEncoder passwordEncoder;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void testFindAllUsersSuccess() throws Exception {
    User user = User.builder()
        .id("1L")
        .email("test@example.com")
        .password("encodedPassword")
        .role("ADMIN")
        .build();

    Claims claims = new DefaultClaims(Map.of("sub", "1L"));

    when(userService.findAllUsers()).thenReturn(List.of(user));

    when(userService.findUserById(user.getId())).thenReturn(user);
    when(jwtService.extractToken("jwtToken")).thenReturn(claims);

    mockMvc.perform(get("/api/v1/users")
        .header("Authorization", "Bearer jwtToken"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$").isNotEmpty())
        .andExpect(jsonPath("$[0].email").value("test@example.com"));
  }

  @Test
  public void testFindAllUsersNonAdminFailure() throws Exception {
    User user = User.builder()
        .id("1L")
        .email("test@example.com")
        .password("encodedPassword")
        .role("USER")
        .build();

    Claims claims = new DefaultClaims(Map.of("sub", "1L"));

    when(userService.findAllUsers()).thenReturn(List.of(user));

    when(userService.findUserById(user.getId())).thenReturn(user);
    when(jwtService.extractToken("jwtToken")).thenReturn(claims);

    mockMvc.perform(get("/api/v1/users")
        .header("Authorization", "Bearer jwtToken"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  public void testFindAllUsersNonAuthenticatedFailure() throws Exception {
    User user = User.builder()
        .id("1L")
        .email("test@example.com")
        .password("encodedPassword")
        .role("USER")
        .build();

    when(userService.findAllUsers()).thenReturn(List.of(user));

    when(userService.findUserById(user.getId())).thenReturn(user);
    when(jwtService.extractToken("jwtToken")).thenThrow(new RuntimeException());

    mockMvc.perform(get("/api/v1/users"))
        .andExpect(status().isForbidden());
  }

  @Test
  public void testCreateUserSuccess() throws Exception {
    User user = User.builder()
        .id("1L")
        .email("test@example.com")
        .password("password")
        .role("USER")
        .build();

    SignInRequest signInRequest = new SignInRequest("test@example.com", "password");

    when(userService.createUser(User.builder().email(signInRequest.email()).password(signInRequest.password()).build()))
        .thenReturn(user);
    mockMvc.perform(post("/api/v1/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(signInRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.userId").value("1L"));
  }

  @Test
  public void testCreateUserEmptyEmailFailure() throws Exception {
    User user = User.builder()
        .id("1L")
        .email("test@example.com")
        .password("password")
        .role("USER")
        .build();

    SignInRequest signInRequest = new SignInRequest("", "password");

    when(userService.createUser(User.builder().email(signInRequest.email()).password(signInRequest.password()).build()))
        .thenReturn(user);
    mockMvc.perform(post("/api/v1/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(signInRequest)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$[0].defaultMessage").value("The email can't be null"));
  }

  @Test
  public void testCreateUserInvalidEmailLengthFailure() throws Exception {
    User user = User.builder()
        .id("1L")
        .email("test@example.com")
        .password("password")
        .role("USER")
        .build();

    SignInRequest signInRequest = new SignInRequest(
        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@gmail.com",
        "password");

    when(userService.createUser(User.builder().email(signInRequest.email()).password(signInRequest.password()).build()))
        .thenReturn(user);
    mockMvc.perform(post("/api/v1/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(signInRequest)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$[0].defaultMessage").value("The email length must be between 4 and 30 characters"));
  }

  @Test
  public void testCreateUserInvalidEmailFormatFailure() throws Exception {
    User user = User.builder()
        .id("1L")
        .email("test@example.com")
        .password("password")
        .role("USER")
        .build();

    SignInRequest signInRequest = new SignInRequest("ta.a", "password");

    when(userService.createUser(User.builder().email(signInRequest.email()).password(signInRequest.password()).build()))
        .thenReturn(user);
    mockMvc.perform(post("/api/v1/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(signInRequest)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$[0].defaultMessage").value("The email is invalid"));
  }

  @Test
  public void testDeleteUserSuccess() throws Exception {
    User user = User.builder()
        .id("1L")
        .email("test@example.com")
        .password("encodedPassword")
        .role("USER")
        .build();

    Claims claims = new DefaultClaims(Map.of("sub", "1L"));

    when(userService.findUserById(user.getId())).thenReturn(user);
    when(jwtService.extractToken("jwtToken")).thenReturn(claims);

    mockMvc.perform(delete("/api/v1/users/" + user.getId())
        .header("Authorization", "Bearer jwtToken"))
        .andExpect(status().isOk());
  }

  @Test
  public void testDeleteUserAdminSuccess() throws Exception {
    User user = User.builder()
        .id("1L")
        .email("test@example.com")
        .password("encodedPassword")
        .role("USER")
        .build();

    User loggedUser = User.builder()
        .id("2L")
        .role("ADMIN")
        .build();

    Claims claims = new DefaultClaims(Map.of("sub", loggedUser.getId()));

    when(userService.findUserById(user.getId())).thenReturn(user);
    when(userService.findUserById(loggedUser.getId())).thenReturn(loggedUser);
    when(jwtService.extractToken("jwtToken")).thenReturn(claims);

    mockMvc.perform(delete("/api/v1/users/" + user.getId())
        .header("Authorization", "Bearer jwtToken"))
        .andExpect(status().isOk());
  }

  @Test
  public void testDeleteUserFailure() throws Exception {
    User user = User.builder()
        .id("1L")
        .email("test@example.com")
        .password("encodedPassword")
        .role("USER")
        .build();

    User loggedUser = User.builder()
        .id("2L")
        .role("USER")
        .build();

    Claims claims = new DefaultClaims(Map.of("sub", loggedUser.getId()));

    when(userService.findUserById(user.getId())).thenReturn(user);
    when(userService.findUserById(loggedUser.getId())).thenReturn(loggedUser);
    when(jwtService.extractToken("jwtToken")).thenReturn(claims);

    mockMvc.perform(delete("/api/v1/users/" + user.getId())
        .header("Authorization", "Bearer jwtToken"))
        .andExpect(status().isUnauthorized());
  }

}
