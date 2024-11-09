package com.example.demo.petcorp.adapter;

import java.io.IOException;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.AbstractMessageConverter;
import org.springframework.util.MimeType;
import com.example.demo.petcorp.shared.dto.PetAdoptionDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Custom Message Converter
 * 
 * @author Igor Peonte <igor.144@gmail.com>
 */
class CustomMessageConverter extends AbstractMessageConverter {

  private ObjectMapper mapper = new ObjectMapper();

  public CustomMessageConverter() {
    super(new MimeType("application", "json"));
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
  }

  @Override
  protected boolean supports(Class<?> clazz) {
    return clazz == PetAdoptionDto.class;
  }

  @Override
  protected Object convertFromInternal(Message<?> message, Class<?> targetClass,
      Object conversionHint) {

    try {
      return mapper.readValue((byte[]) message.getPayload(), PetAdoptionDto.class);
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }

    return null;
  }

  @Override
  protected Object convertToInternal(Object payload, MessageHeaders headers,
      Object conversionHint) {

    try {
      return mapper.writeValueAsBytes(payload);
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }

    return null;
  }
}
