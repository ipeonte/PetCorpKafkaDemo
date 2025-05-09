package com.example.demo.rest.security.auth_server.utils;

/**
 * Authentication Utilities
 */
public class AuthUtils {

  public static final long calcUserMask(String mask) {
    // Get the last digits
    String dmask = mask.substring(mask.lastIndexOf("_") + 1);

    return Integer.parseInt(dmask, 2);
  }

  public static long calcUserMask(String[] masks) {
    int result = 0;
    for (String mask : masks)
      result |= calcUserMask(mask);
    return result;
  }
}
