package com.example.demo.petstore.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.cfg.AvailableSettings;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@SpringBootConfiguration
@EnableAutoConfiguration
@Import(TestChannelBinderConfiguration.class)
public class TestConfiguration {

	@Bean
	PetTestInterceptor petTestInterceptor() {
		return new PetTestInterceptor();
	}

	@Bean
	LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource ds) {
		LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();

		// Set general parameters
		emf.setDataSource(ds);
		emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		emf.setPackagesToScan("com.example.demo.petstore.rest.jpa.model");

		// Set jpa properties
		Properties jp = new Properties();
		// Set query interceptor
		jp.put("hibernate.session_factory.interceptor", petTestInterceptor());
		// Interceptors doesn't work on HQL queries so add statement inspector as well
		jp.put(AvailableSettings.STATEMENT_INSPECTOR, new PetTestSqlInspector());
		emf.setJpaProperties(jp);

		// HSQL DB is used for embedded integration test
		Map<String, Object> jm = new HashMap<>();
		jm.put("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
		emf.setJpaPropertyMap(jm);

		return emf;
	}
}
