/*
 * Copyright (c) 2019 Trevor Robbie
 * This program is made available under the terms of the MIT License.
 */
/**
 *
 */
package trobbie.bootrestsimple.dao;

import java.util.HashMap;
import java.util.Map;

import trobbie.bootrestsimple.model.Resource;

/**
 @author Trevor Robbie
 *
 */
public abstract class TestDatabase<T extends Resource<ID>, ID> {
	// Reference by own index key, not resource ID
	// This way we can refer to test resources by index.
	protected Map<Integer, T> mockResourceMap;
	protected Map<Integer, T> mockResourceMapEmpty;

	protected Integer indexCounter;
	private static final Integer INITIAL_RESOURCE_SET_SIZE = 2;

	public TestDatabase() {
		this.mockResourceMapEmpty = new HashMap<Integer, T>();
		this.mockResourceMap = new HashMap<Integer, T>();
		this.indexCounter = 0;
	}

	/**
	 * Convert index values to ID values. The ID value is not important as long as this method is
	 * a deterministic, one-way function, and does not return value from getIdNeverExist()
	 *
	 * @param index used only as reference for tests
	 * @return ID that the index would represent.
	 */
	public abstract ID indexToResourceId(Integer index);

	/**
	 * Define an id that will never be assigned a resource
	 *
	 * @return an ID that would never be assigned by the database
	 */
	public abstract ID getIdNeverExist();

	/**
	 * Change something; what is changed does not matter
	 *
	 * @param index used only as reference for tests
	 * @return the resource as the index having been changed in some way.
	 */
	public abstract T changeResource(Integer index);

	/**
	 * Create new resource, but don't save to the "database" yet.  Required fields should
	 * be assigned a value, but the id should be null.
	 *
	 * @return a resource that is newly created, yet not saved yet, thus id must be null
	 */
	public abstract T newUnsavedResource();

	/**
	 * Creates a resource object from the JSON string.
	 *
	 * @param jsonSource the string to convert into an object
	 * @return an object of resource type
	 */
	public abstract T asResource(final String jsonSource);

	public void resetData() {
		// ensure empty
		this.mockResourceMapEmpty.clear();

		this.mockResourceMap.clear();
		this.indexCounter = 0;

		for (int i=0; i < INITIAL_RESOURCE_SET_SIZE; i++) {
			saveResource(newUnsavedResource());
		}
	}

	public T getResource(Integer index) {
		return this.mockResourceMap.get(index);
	}

	public Iterable<T> getResources() {
		return this.mockResourceMap.values();
	}

	public Iterable<T> getEmptyResources() {
		return this.mockResourceMapEmpty.values();
	}

	// return index of newly saved resource
	public Integer saveResource(T res) {
		Integer thisTestId = this.indexCounter+1;
		res.setId(indexToResourceId(thisTestId));
		this.mockResourceMap.putIfAbsent(thisTestId, res);
		this.indexCounter = thisTestId;
		return thisTestId;
	}

}
