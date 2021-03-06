/*
 * Copyright (c) 2019 Trevor Robbie
 * This program is made available under the terms of the MIT License.
 */
/**
 *
 */
package trobbie.bootrestsimple.dao;

import com.fasterxml.jackson.databind.ObjectMapper;

import trobbie.bootrestsimple.model.ExampleResource;

/**
 * @author Trevor Robbie
 *
 */
public class ExampleResourceTestDatabase extends TestDatabase<ExampleResource, Long> {

	@Override
	public Long indexToResourceId(Integer index) {
		return Long.valueOf(index);
	}
	@Override
	public Long getIdNeverExist() {
		return 0L; // this id (of valid type) will never be assigned
	}
	@Override
	public ExampleResource newUnsavedResource() {
		return new ExampleResource(null, "MockResource"+(this.indexCounter+1));
	}
	@Override
	public ExampleResource changeResource(Integer index) {
		ExampleResource res = this.mockResourceMap.get(index);
		res.setName(res.getName()+"Updated");  // an example change
		return res;
	}
	@Override
	public ExampleResource asResource(final String jsonSource) {
		try {
			return new ObjectMapper().readValue(jsonSource, ExampleResource.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
