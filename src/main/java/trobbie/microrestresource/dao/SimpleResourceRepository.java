/*
 * Copyright (c) 2019 Trevor Robbie
 * This program is made available under the terms of the MIT License.
 */
/**
 *
 */
package trobbie.microrestresource.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import trobbie.microrestresource.model.SimpleResource;

/**
 * @author Trevor Robbie
 *
 */
public class SimpleResourceRepository implements ResourceRepository<SimpleResource, Long> {
	private static List<SimpleResource> allResources;

	static {
		// TODO: needs data layer implementation
		//   For now, hard-code the data
		allResources = new ArrayList<SimpleResource>();
		allResources.add(new SimpleResource(1L, "TestUser1"));
	}

	@Override
	public SimpleResource save(SimpleResource entity) {
		Optional<SimpleResource> resourceToUpdate = findById(entity.getId());
		if (resourceToUpdate.isPresent()) {
			resourceToUpdate.get().setName(entity.getName());
			return resourceToUpdate.get();
		} else {
			return null;
		}
	}

	@Override
	public <S extends SimpleResource> Iterable<S> saveAll(Iterable<S> entities) {
		//TODO: later implementation
		throw new UnsupportedOperationException();
	}

	@Override
	public Optional<SimpleResource> findById(Long id) {
		return allResources.stream().filter(x -> (x.getId() == id)).findFirst();
	}

	@Override
	public boolean existsById(Long id) {
		return (findById(id) != null);
	}

	@Override
	public List<SimpleResource> findAll() {
		return allResources;
	}

	@Override
	public Iterable<SimpleResource> findAllById(Iterable<Long> ids) {
		//TODO: later implementation
		throw new UnsupportedOperationException();
	}

	@Override
	public long count() {
		return allResources.size();
	}

	@Override
	public void deleteById(Long id) {
		//TODO: later implementation
		throw new UnsupportedOperationException();
	}

	@Override
	public void delete(SimpleResource entity) {
		//TODO: later implementation
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteAll(Iterable<? extends SimpleResource> entities) {
		//TODO: later implementation
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteAll() {
		//TODO: later implementation
		throw new UnsupportedOperationException();
	}


}
