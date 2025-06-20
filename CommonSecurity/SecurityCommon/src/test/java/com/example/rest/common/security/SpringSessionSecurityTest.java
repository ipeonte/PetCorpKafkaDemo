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

package com.example.rest.common.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.net.HttpCookie;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.session.Session;
import org.springframework.session.data.redis.RedisSessionRepository;
import org.springframework.session.data.redis.RedisSessionWrapper;
import org.springframework.test.context.TestPropertySource;

import com.example.rest.common.security.core.Constants;

/**
 * Test cases for Common Security Layer
 *
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:/test.properties")
public class SpringSessionSecurityTest {

	// Default session timeout - 2 sec
	private static final int TEST_SESSION_TIMEOUT = 2;

	@Autowired
	private RedisSessionRepository repository;

	@Autowired
	private TestRestTemplate _rest;

	// Session id
	String sessionId;

	@Test
	public void testAccessForbidden() {
		ResponseEntity<String> response = checkAccessFobidden(makeRequestHttpEntity());
		String cookie = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
		List<HttpCookie> cookies = HttpCookie.parse(cookie);

		// Try same again but with anonymouse session cookie
		checkAccessFobidden(makeRequestHttpEntity(cookies.get(0).getValue()));
	}

	@Test
	public void testInvalidToken() {
		String uuid = UUID.randomUUID().toString();

		// Connect with some random session cookie
		ResponseEntity<String> response = checkAccessFobidden(makeRequestHttpEntity(uuid));

		// Check for same session cookie returns
		String cookie = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
		assertNotNull(cookie);

		String str = Constants.COOKIE_SESSION_NAME + "=";
		assertEquals(0, cookie.indexOf(str));
		assertEquals(-1, cookie.indexOf(str + uuid));
	}

	@Test
	public void testAuthOk() throws InterruptedException {
		checkAuthOk();
	}

	@Test
	public void testSessionExpired() throws InterruptedException {
		// Create session and login
		HttpHeaders headers = checkAuthOk();

		// Wait 100 msec and repeat. Session still valid
		Thread.sleep(100);

		checkTestConnection(headers, HttpStatus.OK, getResponseStr());

		// Wait 1 sec and repeat. Session should expire
		Thread.sleep(TEST_SESSION_TIMEOUT * 1000);

		// Connect using same session
		checkTestConnection(headers, HttpStatus.FORBIDDEN, null);
	}

	private HttpEntity<String> makeRequestHttpEntity() {
		return makeRequestHttpEntity(null);
	}

	/**
	 * Create HTTP Headers and set cookie with Session Id
	 * 
	 * @param id
	 * @return
	 */
	private HttpEntity<String> makeRequestHttpEntity(String id) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.TEXT_HTML));

		if (id != null)
			headers.set(HttpHeaders.COOKIE, Constants.COOKIE_SESSION_NAME + "=" + id);

		HttpEntity<String> entity = new HttpEntity<String>(headers);

		return entity;
	}

	private ResponseEntity<String> checkAccessFobidden(HttpEntity<String> entity) {
		// Anonymous connection
		ResponseEntity<String> resp = _rest.exchange(TestConstants.TEST_URL, HttpMethod.GET, entity, String.class);

		// Expected 403
		assertEquals(HttpStatus.FORBIDDEN, resp.getStatusCode());

		return resp;
	}

	public HttpHeaders checkAuthOk() {
		prepUserSecuritySession();
		HttpHeaders headers = prepHttpHeaders();
		checkTestConnection(headers, HttpStatus.OK, getResponseStr());

		return headers;
	}

	protected void prepMgrSecuritySession() {
		prepSecuritySession(TestConstants.TEST_MGR_NAME, TestConstants.TEST_MGR_ROLES, TEST_SESSION_TIMEOUT);
	}

	protected void prepAdminSecuritySession() {
		prepAdminSecuritySession(TEST_SESSION_TIMEOUT);
	}

	protected void prepAdminSecuritySession(int timeout) {
		prepSecuritySession(TestConstants.TEST_ADMIN_NAME, TestConstants.TEST_ADMIN_ROLES, timeout);
	}

	protected void prepUserSecuritySession() {
		prepUserSecuritySession(TEST_SESSION_TIMEOUT);
	}

	protected void prepUserSecuritySession(int timeout) {
		prepSecuritySession(TestConstants.TEST_USER_NAME, TestConstants.TEST_USER_ROLES, timeout);
	}

	protected void prepSecuritySession(String username, String roles, int timeout) {
		// Create security context for test
		List<GrantedAuthority> rlist = new ArrayList<GrantedAuthority>();
		for (String role : roles.split(","))
			rlist.add(new SimpleGrantedAuthority(role));

		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
				new User(username, "[PROTECTED]", rlist), "[PROTECTED]", rlist);
		SecurityContext context = new SecurityContextImpl();
		context.setAuthentication(token);

		repository.setRedisKeyNamespace(Constants.SESSION_NAMESPACE);

		// For test purposes set low inactive interval 1 sec
		repository.setDefaultMaxInactiveInterval(Duration.ofSeconds(timeout));
		RedisSessionWrapper<Session> wrapper = new RedisSessionWrapper<Session>(repository);
		Session session = wrapper.create(context);

		sessionId = session.getId();
	}

	protected HttpHeaders prepHttpHeaders() {
		// Prepare http headers
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.COOKIE, Constants.COOKIE_SESSION_NAME + "=" + sessionId);

		return headers;
	}

	private String getResponseStr() {
		return Constants.USER_NAME_KEY + ":" + TestConstants.TEST_USER_NAME + "|authenticationName:"
				+ TestConstants.TEST_USER_NAME + "|roles:" + TestConstants.TEST_USER_ROLES + "|sessionId:" + sessionId;
	}

	protected TestRestTemplate getRestTemplate() {
		return _rest;
	}

	protected void checkTestConnection(HttpHeaders headers, HttpStatus status, String bstr) {
		checkGetRequest(headers, status, bstr, TestConstants.TEST_URL);
	}

	protected void checkGetRequestOk(HttpHeaders headers, String bstr, String url) {
		checkGetRequest(headers, HttpStatus.OK, bstr, url);
	}

	private void checkGetRequest(HttpHeaders headers, HttpStatus status, String bstr, String url) {
		HttpEntity<String> entities = new HttpEntity<String>(headers);

		// Connect with previously prepared session id
		ResponseEntity<String> response = _rest.exchange(WebSecurityConfig.API_VER + url, HttpMethod.GET, entities,
				String.class);

		testResponse(response, status, bstr);
	}

	protected void testResponse(ResponseEntity<String> response, HttpStatus status, String bstr) {
		// Check response from success connection
		assertEquals(status, response.getStatusCode());

		if (bstr == null)
			assertNull(response.getBody());
		else {
			assertTrue(response.hasBody());

			// Should be something in a body
			String body = response.getBody();
			assertNotNull(body);

			if (bstr.length() > 0 && bstr.charAt(0) == '^') {
				// Check regex
				if (!Pattern.compile(bstr).matcher(body).matches()) {
					System.out.println(clearJsonPattern(bstr));
					System.out.println(body);
					fail("Response doesn't match provided pattern.");
				}
			} else {
				// Check exact string
				assertEquals(bstr, body);
			}
		}

		// No returned cookies expected
		assertNull(response.getHeaders().getFirst(HttpHeaders.COOKIE));
		assertNull(response.getHeaders().getFirst(HttpHeaders.SET_COOKIE));

	}

	public static String clearJsonPattern(String pattern) {
		String res = pattern.replaceAll("\\\\\\{", "{").replaceAll("\\\\\\[", "[").replaceAll("\\\\\\]", "]")
				.replaceAll("\\\\\\}", "}");

		if (res.charAt(0) == '^')
			res = res.substring(1);

		if (res.charAt(res.length() - 1) == '$')
			res = res.substring(0, res.length() - 1);

		return res;
	}
}

@SpringBootApplication
class SpringSessionTestApp {

}
