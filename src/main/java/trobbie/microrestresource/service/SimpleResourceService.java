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
	public Optional<SimpleResource> getResource(Long id) {
		return resourcesRepository.findById(id);
	}

	@Override
	public SimpleResource replaceResource(SimpleResource specifiedResource) {
		return resourcesRepository.save(specifiedResource);
	}

}
