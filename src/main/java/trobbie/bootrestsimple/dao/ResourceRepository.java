/*
 * Copyright (c) 2019 Trevor Robbie
 * This program is made available under the terms of the MIT License.
 */
/**
 *
 */
package trobbie.bootrestsimple.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import trobbie.bootrestsimple.model.Resource;

/**
 * Interface for resource's repository.  Specify SpringBoot's @repository annotation
 * on the interface extending this one.
 *
 * CRUD refers Create, Read, Update, Delete.
 *
 * @author Trevor Robbie
 */
@NoRepositoryBean  // exclude from being picked up and thus having an instance created for this intermediate interface
public interface ResourceRepository<T extends Resource, ID> extends CrudRepository<T, ID> {

}
