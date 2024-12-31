package us.cloud.teachme.auth_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import us.cloud.teachme.auth_service.model.KafkaTopics;
import us.cloud.teachme.kafkaconfig.service.KafkaUtils;

@Configuration
@Import(us.cloud.teachme.kafkaconfig.config.KafkaConfig.class)
public class KafkaConfig {

  @Bean
  NewTopic userCreatedTopic() {
    return KafkaUtils.createTopic(KafkaTopics.USER_CREATED.getTopic());
  }

  @Bean
  NewTopic userUpdatedTopic() {
    return KafkaUtils.createTopic(KafkaTopics.USER_UPDATED.getTopic());
  }

  @Bean
  NewTopic userDeletedTopic() {
    return KafkaUtils.createTopic(KafkaTopics.USER_DELETED.getTopic());
  }

  @Bean
  NewTopic userActivatedTopic() {
    return KafkaUtils.createTopic(KafkaTopics.USER_ACTIVATED.getTopic());
  }

  @Bean
  NewTopic mailSendTopic() {
    return KafkaUtils.createTopic(KafkaTopics.MAIL_SEND.getTopic());
  }

}
