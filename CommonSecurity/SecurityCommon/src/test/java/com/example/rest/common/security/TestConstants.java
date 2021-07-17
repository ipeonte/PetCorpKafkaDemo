package com.example.rest.common.security;

/**
 * Test Constants
 * 
 * @author Igor Peonte <igor.144@gmail.com>
 *
 */
public class TestConstants {

  public static final String TEST_URL = "/foo";

  public static final String TEST_USER_NAME = "foo";

  public static final String TEST_USER_ROLES = "ROLE_USER,ROLE_FOO";

  public static final String[] TEST_ROLES = TEST_USER_ROLES.split(",");

  public static final String TEST_MGR_NAME = "manager";

  public static final String TEST_MGR_ROLES = "ROLE_MANAGER";

  public static final String TEST_ADMIN_NAME = "admin";

  public static final String TEST_ADMIN_ROLES = "ROLE_ADMIN";

}
