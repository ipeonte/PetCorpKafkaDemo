package com.example.demo.petcorp.adapter.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.TestPropertySource;
import com.example.demo.petcorp.adapter.TestKafkaConfig;
import com.example.demo.petcorp.shared.SharedConstants;
import com.example.demo.petcorp.shared.TestUtils;
import com.example.demo.petcorp.shared.dto.PetAdoptionDto;

@EmbeddedKafka
@SpringBootTest
@Import(TestKafkaConfig.class)
@TestPropertySource("classpath:/test-embedded.properties")
public class EmbeddedKafkaTest {

  @Autowired
  private StreamBridge streamBridge;

  @Test
  public void testOutPetInfoDtoTransormation() throws Exception {
    final long petId = 1;
    final long clientId = 2;

    PetAdoptionDto petAdoption = new PetAdoptionDto(petId, clientId);
    assertNull(petAdoption.getRegistered(), "Registered field not empty");
    String adoptionInfo =
        SharedConstants.MAPPER.writeValueAsString(petAdoption);

    streamBridge.send("adopt-pet", adoptionInfo);

    // Wait 5 sec. for the message to be received
    final int waitTime = 5;
    TestKafkaConfig.COUNT.await(waitTime, TimeUnit.SECONDS);

    // Check if anything received
    assertFalse(TestKafkaConfig.PET_LIST.size() == 0, "Pet List is empty.");
    assertEquals(1, TestKafkaConfig.PET_LIST.size(),
        "Size of Pet List doesn't match.");
    PetAdoptionDto recv = TestKafkaConfig.PET_LIST.get(0);

    assertEquals(petId, recv.getPetId(), "PetId doesn't match.");
    assertEquals(clientId, recv.getClientId(), "ClientId doesn't match.");
    assertNotNull(recv.getRegistered(), "Registered is NULL");

    TestUtils.checkDateStampDiff(
        recv.getRegistered().toEpochSecond(ZoneOffset.UTC), waitTime);
  }
}
