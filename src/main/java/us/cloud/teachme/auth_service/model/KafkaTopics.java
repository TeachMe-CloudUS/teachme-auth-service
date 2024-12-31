package us.cloud.teachme.auth_service.model;

public enum KafkaTopics {
  USER_CREATED("user-created"),
  USER_UPDATED("user-updated"),
  USER_DELETED("user-deleted"),
  USER_ACTIVATED("user-activated"),
  MAIL_SEND("mail-send");

  private final String topic;

  KafkaTopics(String topic) {
    this.topic = topic;
  }

  public String getTopic() {
    return topic;
  }
}
