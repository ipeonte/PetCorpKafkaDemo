/*
 * Seerema Business Solutions - http://www.seerema.com/
 * 
 * Copyright 2020 IvaLab Inc. and by respective contributors (see below).
 * 
 * Released under the LGPL v3 or higher See http://www.gnu.org/licenses/lgpl.txt
 *
 * Contributors:
 * 
 */

package org.springframework.session.data.redis;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import com.example.rest.common.security.TestConstants;
import com.example.rest.common.security.core.Constants;

/**
 * RedisSessionWrapper
 * 
 * @see https://github.com/spring-projects/spring-session/issues/79
 * 
 * @param <S> Session Type
 */
public class RedisSessionWrapper<S extends Session> {

  private SessionRepository<S> repository;

  @SuppressWarnings("unchecked")
  public RedisSessionWrapper(RedisSessionRepository repository) {
    this.repository = (SessionRepository<S>) repository;
  }

  public S create(SecurityContext context) {
    S s = repository.createSession();
    // Inject test attributes
    s.setAttribute(Constants.USER_NAME_KEY, TestConstants.TEST_USER_NAME);
    s.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
    repository.save(s);

    return s;
  }
}
