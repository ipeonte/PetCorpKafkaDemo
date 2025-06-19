package com.example.demo.petstore.rest;

import org.hibernate.CallbackException;
import org.hibernate.Interceptor;
import org.hibernate.type.Type;

public class PetTestInterceptor implements Interceptor {

	@Override
	public boolean onSave(Object entity, Object id, Object[] state, String[] propertyNames, Type[] types)
			throws CallbackException {
		return checkError();
	}

	@Override
	public boolean onLoad(Object entity, Object id, Object[] state, String[] propertyNames, Type[] types)
			throws CallbackException {
		return checkError();
	}

	static boolean checkError() throws CallbackException {
		if (TestConstants.FL_ERROR)
			throw new CallbackException(TestConstants.TEST_ERR_MSG);

		return false;
	}
}
