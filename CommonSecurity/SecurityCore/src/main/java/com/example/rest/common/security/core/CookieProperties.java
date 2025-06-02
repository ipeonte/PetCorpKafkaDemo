package com.example.rest.common.security.core;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Shared class with cookie properties
 */
@Component
@ConfigurationProperties("cookie")
public class CookieProperties {
  private String domain;

  private String path;

  private String protocol;
  
  public String getDomain() {
    return domain == null ? Constants.DEF_COOKIE_DOMAIN : domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }

  public String getPath() {
    return path == null ? Constants.DEF_COOKIE_PATH : path;
  }

  public void setPath(String path) {
    this.path = path;
  }
  
  public String getProtocol() {
	return protocol == null ? Constants.DEF_PROTOCOL : protocol;
  }
  
  public void setProtocol(String protocol) {
	this.protocol = protocol;
  }
  
  public String getRedirectPath() {
	return getProtocol() + "://" + getDomain() + getPath();
  }
}
