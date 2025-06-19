package com.example.demo.petstore.rest;

import org.hibernate.resource.jdbc.spi.StatementInspector;

public class PetTestSqlInspector implements StatementInspector {

	// Default Serial Version UID
	private static final long serialVersionUID = 1L;

	@Override
	public String inspect(String sql) {
		if (TestConstants.FL_ERROR)
			throw new RuntimeException(TestConstants.TEST_ERR_MSG);
		return null;
	}

}
