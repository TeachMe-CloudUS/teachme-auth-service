package us.cloud.teachme.auth_service.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import us.cloud.teachme.auth_service.exceptions.UserNotFoundException;
import us.cloud.teachme.auth_service.model.User;
import us.cloud.teachme.auth_service.repository.UserRepository;
import us.cloud.teachme.auth_service.request.RegisterRequest;
import us.cloud.teachme.auth_service.request.SignInRequest;
import us.cloud.teachme.auth_service.service.JwtService;
import us.cloud.teachme.auth_service.service.MailService;
import us.cloud.teachme.auth_service.service.UserService;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class AuthControllerTest {

  @MockBean
  private UserService userService;

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private JwtService jwtService;

  @MockBean
  private PasswordEncoder passwordEncoder;

  @MockBean
  private MailService mailService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void testSignInSuccess() throws Exception {
    User user = User.builder()
        .id("1L")
        .email("test@example.com")
        .password("encodedPassword")
        .build();

    SignInRequest signInRequest = new SignInRequest("test@example.com", "password");

    when(userService.findUserByEmail(user.getEmail())).thenReturn(user);
    when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
    when(jwtService.generateToken(user)).thenReturn("jwtToken");

    mockMvc.perform(post("/api/v1/auth/signin")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(signInRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").value("jwtToken"));
  }

  @Test
  public void testSignInFailure() throws Exception {
    SignInRequest signInRequest = new SignInRequest("test@example.com", "password");

    when(userService.findUserByEmail("test@example.com")).thenThrow(new UserNotFoundException());

    mockMvc.perform(post("/api/v1/auth/signin")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(signInRequest)))
        .andExpect(status().isNotFound());

  }

  @Test
  public void testSignInBadCredentials() throws Exception {
    User user = User.builder()
        .id("1L")
        .email("test@example.com")
        .password("encodedPassword")
        .build();

    SignInRequest signInRequest = new SignInRequest("test@example.com", "password");

    when(userService.findUserByEmail("test@example.com")).thenReturn(user);
    when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(false);

    mockMvc.perform(post("/api/v1/auth/signin")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(signInRequest)))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testRegisterSuccess() throws Exception {
    RegisterRequest registerRequest = new RegisterRequest("test@example.com", "password");

    mockMvc.perform(post("/api/v1/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(registerRequest)))
        .andExpect(status().isNotImplemented());
  }

  @Test
  public void testValidate() throws Exception {
    User user = User.builder().id("1L").email("test@example.com").build();

    when(jwtService.extractToken("jwtToken")).thenReturn(new DefaultClaims(Map.of("sub", "1L")));
    when(userService.findUserById("1L")).thenReturn(user);

    mockMvc.perform(get("/api/v1/auth/validate")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"token\": \"jwtToken\"}"))
        .andExpect(status().isNoContent());
  }

  @Test
  public void testMe() throws Exception {
    User user = User.builder().id("1L").email("test@example.com").role("USER").build();

    Claims claims = new DefaultClaims(Map.of("sub", "1L"));

    when(userService.findUserById("1L")).thenReturn(user);
    when(jwtService.extractToken("jwtToken")).thenReturn(claims);

    mockMvc.perform(get("/api/v1/auth/me")
        .header("Authorization", "Bearer jwtToken")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"token\": \"jwtToken\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email").value("test@example.com"));
  }
}
