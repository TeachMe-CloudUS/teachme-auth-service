package us.cloud.teachme.auth_service.model;

public enum KafkaTopics {
  USER_CREATED("auth-service", "user", "created"),
  USER_UPDATED("auth-service", "user", "updated"),
  USER_DELETED("auth-service", "user", "deleted"),
  USER_ACTIVATED("auth-service", "user", "activated"),
  MAIL_SEND("auth-service", "mail", "send");

  private final String service;

  private final String entity;

  private final String action;

  KafkaTopics(String service, String entity, String action) {
    this.service = service;
    this.entity = entity;
    this.action = action;
  }

  public String getTopic() {
    return service + "." + entity + "." + action;
  }
}
