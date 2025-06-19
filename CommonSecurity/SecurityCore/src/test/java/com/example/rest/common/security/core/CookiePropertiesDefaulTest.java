package com.example.rest.common.security.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = { "spring.main.banner-mode=off" })
class CookiePropertiesDefaulTest {

	@Autowired
	private CookieProperties cp;

	@Test
	void test() {
		assertEquals(Constants.DEF_PROTOCOL, cp.getProtocol());
		assertEquals(Constants.DEF_COOKIE_DOMAIN, cp.getDomain());
		assertEquals(Constants.DEF_COOKIE_PATH, cp.getPath());
		assertEquals(Constants.DEF_PROTOCOL + "://" + Constants.DEF_COOKIE_DOMAIN + Constants.DEF_COOKIE_PATH,
				cp.getRedirectPath());
	}
}

@SpringBootConfiguration
@EnableAutoConfiguration
class TestConfig {

}
