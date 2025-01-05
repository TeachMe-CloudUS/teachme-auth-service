package us.cloud.teachme.auth_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import us.cloud.teachme.auth_service.filters.JwtAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
        .cors(cors -> cors.disable())
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers("/api/v1/auth/**").permitAll()
            .requestMatchers("/api/v1/auth/validate").authenticated()
            .requestMatchers(HttpMethod.POST, "/api/v1/users").permitAll()
            .requestMatchers("/api/v1/auth/activate").permitAll()
            .requestMatchers("/api/v1/users/**").authenticated()
            .requestMatchers("/swagger-ui/**").permitAll()
            .requestMatchers("/swagger/**").permitAll()
            .requestMatchers("/api/health").permitAll()
            .anyRequest().denyAll())
        .sessionManagement(session -> session.disable())
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }

}
