package com.example.demo.petcorp.shared;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Test Utilities
 * 
 * @author Igor Peonte <igor.144@gmail.com>
 */
public class TestUtils {

  /**
   * Check if DateTime Stamp is around "now"
   * @param seconds the Epoc Seconds
   * @param maxDiff the Max Differentce between given Epoc seconds and "now"
   */
  public static void checkDateStampDiff(long seconds, long maxDiff) {
    LocalDateTime dts = LocalDateTime.now(ZoneOffset.UTC);
    long diff = dts.toEpochSecond(ZoneOffset.UTC) - seconds;
    assertTrue(diff <= maxDiff, "Difference between seconds  " + diff +
        " is greater than " + maxDiff + " second");
  }
}
