package us.cloud.teachme.auth_service.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HealthCheckController {

  @GetMapping("/health")
  public ResponseEntity<Map<String, String>> healthCheck() {
    return ResponseEntity.ok().body(Map.of("status", "UP"));
  }
  
}
