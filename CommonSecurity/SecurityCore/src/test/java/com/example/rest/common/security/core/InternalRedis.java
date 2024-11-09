package com.example.rest.common.security.core;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import redis.embedded.RedisServer;

/**
 * Internal Redis component
 * 
 * @author Igor Peonte <igor.144@gmail.com>
 *
 */
@Component
public class InternalRedis {

  private static final Logger LOG = LoggerFactory.getLogger(InternalRedis.class);

  private int port;

  private RedisServer server;

  @Autowired
  public void setConnectionFactory(JedisConnectionFactory factory) throws IOException {
    this.port = factory.getPort();
    this.server = new RedisServer(this.port);
  }

  @PostConstruct
  public void startRedis() throws IOException {
    LOG.info("Starting Internal Redis. Port: " + port);
    server.start();
  }

  @PreDestroy
  public void stopRedis() {
    server.stop();
  }
}
