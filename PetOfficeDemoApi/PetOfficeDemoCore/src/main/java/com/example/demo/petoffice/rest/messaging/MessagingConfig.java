package com.example.demo.petoffice.rest.messaging;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.petcorp.shared.SharedConstants;
import com.example.demo.petcorp.shared.dto.PetAdoptionDto;
import com.example.demo.petoffice.rest.error.PetOfficeExeption;
import com.example.demo.petoffice.rest.service.ClientService;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Auto Configuration class
 */

@Configuration
public class MessagingConfig {

  @Autowired
  ClientService clientService;

  @Bean
  public Consumer<String> consumeAdoptionEvent() {
    return adoptionInfo -> {
      SharedConstants.LOG.info("Received Adoption Message - " + adoptionInfo);

      PetAdoptionDto petAdoption;
      try {
        petAdoption = SharedConstants.MAPPER.readValue(adoptionInfo,
            PetAdoptionDto.class);
      } catch (JsonProcessingException e) {
        SharedConstants.LOG.error(
            "Error convertion '{}' into PetAdoptionDto - {}", adoptionInfo,
            e.getMessage());
        return;
      }

      try {
        clientService.adoptPet(petAdoption);
      } catch (PetOfficeExeption e) {
        // Log for now
        SharedConstants.LOG.error(e.getMessage());
      }
    };
  }
}
