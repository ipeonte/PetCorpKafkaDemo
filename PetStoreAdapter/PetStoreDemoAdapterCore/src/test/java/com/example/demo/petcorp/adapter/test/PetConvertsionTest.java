package com.example.demo.petcorp.adapter.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;
import java.time.ZoneOffset;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import com.example.demo.petcorp.shared.SharedConstants;
import com.example.demo.petcorp.shared.TestUtils;
import com.example.demo.petcorp.shared.dto.PetAdoptionDto;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@SpringBootTest
@DirtiesContext
@Import(TestChannelBinderConfiguration.class)
@TestPropertySource("classpath:/test.properties")
public class PetConvertsionTest {

  @Autowired
  private InputDestination input;

  @Autowired
  private OutputDestination output;

  @Test
  public void testOutPetInfoDtoTransormation()
      throws JsonParseException, JsonMappingException, IOException {
    final long petId = 1;
    final long clientId = 2;

    PetAdoptionDto in = new PetAdoptionDto(petId, clientId);
    assertNull(in.getRegistered(), "Registered field not empty");

    input.send(new GenericMessage<byte[]>(SharedConstants.MAPPER.writeValueAsBytes(in)));

    byte[] data = output.receive().getPayload();
    PetAdoptionDto out = SharedConstants.MAPPER.readValue(data, PetAdoptionDto.class);

    assertEquals(petId, out.getPetId(), "Pet Id doesn't match");
    assertEquals(clientId, out.getClientId(), "Client Id doesn't match");
    assertNotNull(out.getRegistered());

    TestUtils.checkDateStampDiff(out.getRegistered().toEpochSecond(ZoneOffset.UTC), 1);
  }
}
