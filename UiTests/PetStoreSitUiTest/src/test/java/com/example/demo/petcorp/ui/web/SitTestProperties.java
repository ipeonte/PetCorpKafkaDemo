package com.example.demo.petcorp.ui.web;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * SIT Test Properties
 * 
 * @author Igor Peonte <igor.144@gmail.com>
 *
 */

@Component
@ConfigurationProperties("sit-test")
public class SitTestProperties {

  // Base URL to start test from
  private String baseUrl;

  public String getBaseUrl() {
    return baseUrl;
  }

  public void setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
  }
}
