package com.example.demo.petcorp.ui.web.test;

/**
 * Test Utilities
 * 
 * @author Igor Peonte <igor.144@gmail.com>
 *
 */
public class TestUtils {

  // Demo mode
  private static final boolean DEMO_MODE = false;

  public static void sleep() {
    sleep(DEMO_MODE ? 5 : 1);
  }

  public static void sleep(int tsec) {
    try {
      Thread.sleep((DEMO_MODE ? tsec + 5 : tsec) * 1000);
    } catch (InterruptedException e) {
      // Do nothing
    }
  }
}
