/*
 * Copyright (c) 2019 Trevor Robbie
 * This program is made available under the terms of the MIT License.
 */
/**
 *
 */
package trobbie.microrestresource.dao;

import org.springframework.data.repository.CrudRepository;

import trobbie.microrestresource.model.Resource;

/**
 * @author Trevor Robbie
 *
 */
public interface ResourceRepository<T extends Resource, ID> extends CrudRepository<T, ID> {

}
