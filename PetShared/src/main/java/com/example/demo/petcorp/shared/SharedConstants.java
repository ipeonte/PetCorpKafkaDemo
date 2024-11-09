package com.example.demo.petcorp.shared;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Global Constants
 * 
 * @author Igor Peonte <igor.144@gmail.com>
 *
 */
public class SharedConstants {

  // Base URL for all API calls
  public static final String BASE_URL = "/api/v1";

  // Shared Demo logger
  public static final Logger LOG = LoggerFactory.getLogger("demo");

  public static final ObjectMapper MAPPER;

  static {
    // Define and configure mapper
    MAPPER = new ObjectMapper();
    MAPPER.registerModule(new JavaTimeModule());
    MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    MAPPER.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
  }
}
