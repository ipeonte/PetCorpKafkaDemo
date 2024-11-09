package com.example.rest.common.security.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.test.util.TestSocketUtils;

/**
 * Configuration for Internal Redis
 * 
 * @author Igor Peonte <igor.144@gmail.com>
 *
 */
@Configuration
public class InternalRedisConfig {
  /**
   * Find random TCP port for internal Redis Use localhost as defalut host
   *
   * @return RedisConnectionFactory
   */
  @Bean
  @Primary
  JedisConnectionFactory jedisConnectionFactory() {
    JedisConnectionFactory factory = new JedisConnectionFactory(
        new RedisStandaloneConfiguration("localhost", TestSocketUtils.findAvailableTcpPort()));

    return factory;
  }
}
