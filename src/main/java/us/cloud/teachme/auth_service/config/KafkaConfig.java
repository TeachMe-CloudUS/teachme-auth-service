package us.cloud.teachme.auth_service.config;

import java.util.Map;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import us.cloud.teachme.auth_service.model.KafkaTopics;

@Configuration
@ConditionalOnProperty(value = "spring.kafka.enabled", havingValue = "true")
public class KafkaConfig {

  @Value("${spring.kafka.bootstrap-servers}")
  private String bootstrapServers;

  @Bean
  NewTopic userCreatedTopic() {
    return TopicBuilder.name(KafkaTopics.USER_CREATED.getTopic())
        .build();
  }

  @Bean
  NewTopic userUpdatedTopic() {
    return TopicBuilder.name(KafkaTopics.USER_UPDATED.getTopic())
        .build();
  }

  @Bean
  NewTopic userDeletedTopic() {
    return TopicBuilder.name(KafkaTopics.USER_DELETED.getTopic())
        .build();
  }

  @Bean
  NewTopic userActivatedTopic() {
    return TopicBuilder.name(KafkaTopics.USER_ACTIVATED.getTopic())
        .build();
  }

  @Bean
  NewTopic mailSendTopic() {
    return TopicBuilder.name(KafkaTopics.MAIL_SEND.getTopic())
        .build();
  }

  @Bean
  Map<String, Object> producerProps() {
    return Map.of(
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
  }

  @Bean
  ProducerFactory<String, Object> producerFactory() {
    return new DefaultKafkaProducerFactory<>(producerProps());
  }

  @Bean
  KafkaTemplate<String, Object> kafkaTemplate() {
    return new KafkaTemplate<>(producerFactory());
  }

}
