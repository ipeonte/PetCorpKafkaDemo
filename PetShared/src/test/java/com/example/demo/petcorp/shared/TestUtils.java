package com.example.demo.petcorp.shared;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Test Utilities
 * 
 * @author Igor Peonte <igor.144@gmail.com>
 */
public class TestUtils {

  // Default wait time on process completion
  public static final int WAIT_TIME = 5;

  /**
   * Check if DateTime Stamp is around "now"
   * 
   * @param seconds the Epoc Seconds
   * @param maxDiff the Max Differentce between given Epoc seconds and "now"
   */
  public static void checkDateStampDiff(long seconds, long maxDiff) {
    LocalDateTime dts = LocalDateTime.now(ZoneOffset.UTC);
    long diff = dts.toEpochSecond(ZoneOffset.UTC) - seconds;
    assertTrue(diff <= maxDiff,
        "Difference between seconds  " + diff + " is greater than " + maxDiff + " second");
  }

  public static boolean isDebug() {
    return java.lang.management.ManagementFactory.getRuntimeMXBean().getInputArguments().toString()
        .contains("-agentlib:jdwp");
  }

  public static void waitCountDown(CountDownLatch count) throws InterruptedException {
    count.await(isDebug() ? WAIT_TIME * 100 : WAIT_TIME, TimeUnit.SECONDS);
  }

  public static String getFileNameWithoutExt(Path path) {
    String fname = path.getFileName().toString();
    int pos = fname.lastIndexOf(".");
    return (pos == -1) ? fname : fname.substring(0, pos);
  }
}
