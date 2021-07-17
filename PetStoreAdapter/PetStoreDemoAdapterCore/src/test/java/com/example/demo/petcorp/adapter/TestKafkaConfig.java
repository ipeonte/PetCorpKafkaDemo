package com.example.demo.petcorp.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binder.kafka.config.ConsumerConfigCustomizer;
import org.springframework.cloud.stream.binder.kafka.config.ProducerConfigCustomizer;
import org.springframework.cloud.stream.binder.kafka.provisioning.AdminClientConfigCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import com.example.demo.petcorp.shared.SharedConstants;
import com.example.demo.petcorp.shared.dto.PetAdoptionDto;
import com.fasterxml.jackson.core.JsonProcessingException;

public class TestKafkaConfig {

  public static final CountDownLatch COUNT = new CountDownLatch(1);

  public static final List<PetAdoptionDto> PET_LIST = new ArrayList<>();

  @Autowired
  private EmbeddedKafkaBroker broker;

  @Bean
  ConsumerConfigCustomizer consumerConfigCustomizer() {
    return (props, bindingName, destination) -> {
      Map<String, Object> properties =
          KafkaTestUtils.consumerProps("test", "true", broker);

      // Set ByteArray deserializers
      properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
          ByteArrayDeserializer.class);
      properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
          ByteArrayDeserializer.class);

      props.putAll(properties);
    };
  }

  @Bean
  ProducerConfigCustomizer producerConfigCustomizer() {
    return (props, bindingName, destination) -> {
      Map<String, Object> properties = KafkaTestUtils.producerProps(broker);

      // Set ByteArray serializers
      properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
          ByteArraySerializer.class);
      properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
          ByteArraySerializer.class);

      props.putAll(properties);
    };
  }

  @Bean
  public AdminClientConfigCustomizer adminClientConfigCustomizer() {
    return props -> {
      props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG,
          broker.getBrokersAsString());
    };
  }

  @Bean
  public Consumer<String> recvAdoptionInfo() {
    return adoptInfo -> {
      try {
        PET_LIST.add(
            SharedConstants.MAPPER.readValue(adoptInfo, PetAdoptionDto.class));
        COUNT.countDown();
      } catch (JsonProcessingException e) {
        SharedConstants.LOG.error(e.getMessage());
      }
    };
  }
}
