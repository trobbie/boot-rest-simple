package trobbie.microrestjson.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import trobbie.microrestjson.dao.ResourceRepository;
import trobbie.microrestjson.model.Resource;

/**
 * Default resource service that can be used out of the box for base functionality.
 *
 * This must still be implemented to define the concrete resource model to use, along with its
 * String-to-ID converter function.
 *
 * @author Trevor Robbie
 *
 */
@Service
public abstract class DefaultResourceService<T extends Resource, ID> implements ResourceService<T, ID> {

	@Autowired  // mark as being injected (wired by type)
	private ResourceRepository<T, ID> resourceRepository;

	@Override
	public Iterable<T> getResources() {
		return resourceRepository.findAll();
	}

	@Override
	public Optional<T> getResource(String idString) {
		ID id = this.stringToIDConverter(idString);
		if (id == null) return null;
		return resourceRepository.findById(id);
	}

	@Override
	public Optional<T> saveResource(T specifiedResource) {
		if (specifiedResource == null) return Optional.empty();
		return Optional.of(resourceRepository.save(specifiedResource));
	}

	@Override
	public Optional<T> createResource(T specifiedResource) {
		if (specifiedResource == null) return Optional.empty();
		if (specifiedResource.getId() != null) throw new IllegalArgumentException();
		// this save will update the id
		return Optional.of(resourceRepository.save(specifiedResource));
	}

	@Override
	public Boolean deleteResource(String idString) {
		ID id = this.stringToIDConverter(idString);
		if (id == null) return null;
		resourceRepository.deleteById(id);
		return true;
	}


}
