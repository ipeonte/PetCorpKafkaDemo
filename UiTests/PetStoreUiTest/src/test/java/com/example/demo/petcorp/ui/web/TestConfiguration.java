package com.example.demo.petcorp.ui.web;

import java.util.Map;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.stream.binder.kafka.provisioning.AdminClientConfigCustomizer;
import org.springframework.cloud.stream.binder.kafka.support.ConsumerConfigCustomizer;
import org.springframework.cloud.stream.binder.kafka.support.ProducerConfigCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;

@ComponentScan
@SpringBootConfiguration
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.example.demo.petstore.rest.jpa.model",
    "com.example.demo.petoffice.rest.jpa.model"})
@EnableJpaRepositories(basePackages = {"com.example.demo.petstore.rest.jpa.repo",
    "com.example.demo.petoffice.rest.jpa.repo"})
public class TestConfiguration {

  @Autowired
  private EmbeddedKafkaBroker broker;

  @Bean
  ConsumerConfigCustomizer consumerConfigCustomizer() {
    return (props, bindingName, destination) -> {
      Map<String, Object> properties = KafkaTestUtils.consumerProps("test", "true", broker);

      // Set ByteArray deserializers
      properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);
      properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);

      props.putAll(properties);
    };
  }

  @Bean
  ProducerConfigCustomizer producerConfigCustomizer() {
    return (props, bindingName, destination) -> {
      Map<String, Object> properties = KafkaTestUtils.producerProps(broker);

      // Set ByteArray serializers
      properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
      properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);

      props.putAll(properties);
    };
  }

  @Bean
  AdminClientConfigCustomizer adminClientConfigCustomizer() {
    return props -> {
      props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, broker.getBrokersAsString());
    };
  }
}
