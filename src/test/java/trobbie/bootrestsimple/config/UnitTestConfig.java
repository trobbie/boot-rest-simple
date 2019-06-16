/*
 * Copyright (c) 2019 Trevor Robbie
 * This program is made available under the terms of the MIT License.
 */
/**
 *
 */
package trobbie.bootrestsimple.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import trobbie.bootrestsimple.dao.ExampleResourceTestDatabase;
import trobbie.bootrestsimple.dao.TestDatabase;
import trobbie.bootrestsimple.model.ExampleResource;

/**
 * @author Trevor Robbie
 *
 */
@Profile("test")
@Configuration // denotes class as having @Bean definitions used to configure
@ComponentScan("trobbie.bootrestsimple")
public class UnitTestConfig {
	@Bean
	public TestDatabase<ExampleResource, Long> testDatabase() {
		return new ExampleResourceTestDatabase();
	}
}
