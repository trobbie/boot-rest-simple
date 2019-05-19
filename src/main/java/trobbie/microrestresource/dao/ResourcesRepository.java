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

import org.springframework.data.repository.CrudRepository;

import trobbie.microrestresource.model.Resource;

/**
 * @author Trevor Robbie
 *
 */
public class ResourcesRepository implements CrudRepository<Resource, Long>{
	private static List<Resource> allResources;

	static {
		// TODO: needs data layer implementation
		//   For now, hard-code the data
		allResources = new ArrayList<Resource>();
		allResources.add(new Resource(1L, "TestUser1"));
	}


	@Override
	public Resource save(Resource entity) {
		Optional<Resource> resourceToUpdate = findById(entity.getId());
		if (resourceToUpdate.isPresent()) {
			resourceToUpdate.get().setName(entity.getName());
			return resourceToUpdate.get();
		} else {
			return null;
		}
	}

	@Override
	public <S extends Resource> Iterable<S> saveAll(Iterable<S> entities) {
		//TODO: later implementation
		throw new UnsupportedOperationException();
	}

	@Override
	public Optional<Resource> findById(Long id) {
		return allResources.stream().filter(x -> (x.getId() == id)).findFirst();
	}

	@Override
	public boolean existsById(Long id) {
		return (findById(id) != null);
	}

	@Override
	public List<Resource> findAll() {
		return allResources;
	}

	@Override
	public Iterable<Resource> findAllById(Iterable<Long> ids) {
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
	public void delete(Resource entity) {
		//TODO: later implementation
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteAll(Iterable<? extends Resource> entities) {
		//TODO: later implementation
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteAll() {
		//TODO: later implementation
		throw new UnsupportedOperationException();
	}


}
