/*
 * Copyright (c) 2019 Trevor Robbie
 * This program is made available under the terms of the MIT License.
 */
/**
 *
 */
package trobbie.microrestresource.dao;

import org.springframework.stereotype.Repository;

import trobbie.microrestresource.model.SimpleResource;

/**
 * Interface used by SpringBoot's repository annotation.
 *
 * Implementation configuration in spring.datasource in application.properties)
 *
 * @author Trevor Robbie
 *
 */
@Repository
public interface SimpleResourceRepository extends ResourceRepository<SimpleResource, Long> {
	// No need to write implementation. Spring Data JPA creates it for us on the fly,
	// based on what implementation is found on the classpath.
}
