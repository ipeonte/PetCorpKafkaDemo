package com.example.demo.petcorp.adapter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.function.Function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.messaging.converter.MessageConverter;

import com.example.demo.petcorp.shared.SharedConstants;
import com.example.demo.petcorp.shared.dto.PetAdoptionDto;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Adapter Auto Configuration class
 * 
 * @author Igor Peonte <igor.144@gmail.com>
 */
@ComponentScan("com.example.demo.petcorp.adapter")
public class AutoConfiguration {

  @Bean
  Function<String, String> newPetProcessing() {
    return adoptInfo -> {
      // Map string into PetAdoptionDto
      PetAdoptionDto petAdoption;
      try {
        petAdoption =
            SharedConstants.MAPPER.readValue(adoptInfo, PetAdoptionDto.class);
      } catch (JsonProcessingException e) {
        SharedConstants.LOG.error(
            "Error converting '{}' into PetAdoptionDto - {}", adoptInfo,
            e.toString());
        return null;
      }

      petAdoption.setRegistered(LocalDateTime.now(ZoneOffset.UTC));
      SharedConstants.LOG
          .info("Registering Pet Adoption Request - " + petAdoption);
      try {
        return SharedConstants.MAPPER.writeValueAsString(petAdoption);
      } catch (JsonProcessingException e) {
        SharedConstants.LOG.error(
            "Error converting PetAdoptionDto '{}' into String - {}",
            petAdoption, e.toString());
        return null;
      }
    };
  }

  // @Bean
  public MessageConverter customMessageConverter() {
    return new CustomMessageConverter();
  }
}
