/*
 * Copyright (c) 2019 Trevor Robbie
 * This program is made available under the terms of the MIT License.
 */
/**
 *
 */
package trobbie.microrestresource.dao;

import org.springframework.stereotype.Repository;

import trobbie.microrestresource.model.ExampleResource;

/**
 * Interface used as SpringBoot's repository.  For SpringBoot to autowire, 
 * this interface needs to be defined extending the CRUDRepository with the
 * specific model used (Resource subclass) and @Repository assigned.
 *
 * Implementation configuration found in spring.datasource in application.properties.
 *
 * @author Trevor Robbie
 *
 */
@Repository // assign as JPA Repository
public interface ExampleResourceRepository extends ResourceRepository<ExampleResource, Long> {
	// No need to write implementation. Spring Data JPA creates it for us on the fly,
	// based on what implementation is found on the classpath.
}
