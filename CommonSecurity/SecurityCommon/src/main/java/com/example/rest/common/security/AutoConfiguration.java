package com.example.rest.common.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.CookieHttpSessionIdResolver;
import org.springframework.session.web.http.DefaultCookieSerializer;

import com.example.rest.common.security.core.Constants;

/**
 * AutoConfiguration file for shared library
 * 
 * @author Igor Peonte <igor.144@gmail.com>
 *
 */
@Configuration
@ComponentScan("com.example.rest.common.security")
public class AutoConfiguration {

  @Bean
  public CookieHttpSessionIdResolver cookieHttpSessionIdResolver() {
    CookieHttpSessionIdResolver resolver = new CookieHttpSessionIdResolver();
    DefaultCookieSerializer serializer = new DefaultCookieSerializer();
    serializer.setCookieName(Constants.COOKIE_SESSION_NAME);
    serializer.setUseBase64Encoding(false);
    resolver.setCookieSerializer(serializer);

    return resolver;
  }
}
