package com.example.rest.common.security.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * Configuration class for RedisHttpSession Define special namespace to store session data
 *
 * @author Igor Peonte <igor.144@gmail.com>
 *
 */
@Configuration
@EnableRedisHttpSession(redisNamespace = Constants.SESSION_NAMESPACE)
public class Config {

  @Autowired
  private CookieProperties cookieProperties;

  @Bean
  CookieSerializer cookieSerializer() {
    DefaultCookieSerializer s = new DefaultCookieSerializer();
    s.setCookiePath(cookieProperties.getPath());
    s.setUseBase64Encoding(false);
    s.setDomainName(cookieProperties.getDomain());
    s.setCookieName(Constants.COOKIE_SESSION_NAME);

    return s;
  }

}
