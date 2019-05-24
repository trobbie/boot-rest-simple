package trobbie.microrestresource.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import trobbie.microrestresource.dao.SimpleResourceRepository;
import trobbie.microrestresource.model.SimpleResource;

/**
 *
 *
 * @author Trevor Robbie
 *
 */
@Component
public class SimpleResourceService implements ResourceService<SimpleResource, Long>{

	@Autowired  // mark as being injected (wired by type)
	private SimpleResourceRepository resourcesRepository;

	@Override
	public Iterable<SimpleResource> getResources() {
		return resourcesRepository.findAll();
	}

	@Override
	public SimpleResource getResource(Long id) {
		Optional<SimpleResource> result = resourcesRepository.findById(id);
		if (result.isPresent())
			return result.get();
		else
			return null;
	}

	@Override
	public SimpleResource replaceResource(SimpleResource specifiedResource) {
		return resourcesRepository.save(specifiedResource);
	}

}
