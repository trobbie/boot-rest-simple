package trobbie.microrestresource.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import trobbie.microrestresource.dao.ResourcesRepository;
import trobbie.microrestresource.model.Resource;

/**
 *
 *
 * @author Trevor Robbie
 *
 */
@Component
public class ResourceService {

	private ResourcesRepository resourcesRepository;

	/**
	 * Returns list of resources. Returns empty list if no resources.
	 */
	public List<Resource> getResources() {
		return resourcesRepository.findAll();
	}

	/**
	 * Returns resource object, given the id of the resource. Returns null if id not found.
	 *
	 * @param id the id of the resource
	 * @return the retrieved resource object, or null if not found
	 */
	public Resource getResource(Long id) {
		Optional<Resource> result = resourcesRepository.findById(id);
		if (result.isPresent())
			return result.get();
		else
			return null;
	}

	/**
	 * Replaces the resource object at the specified resource's id.  If resource's id does not already
	 * exist, then return null.
	 *
	 * <p> Note: this method must be idempotent.
	 * <p> Note: if only certain fields are to be updated (e.g. HTTP method: PATCH), use another method.
	 *
	 * @param specifiedResource the resource containing the new values
	 * @return the replaced resource object, or null if id could not be found
	 */
	public Resource replaceResource(Resource specifiedResource) {
		return resourcesRepository.save(specifiedResource);
	}

}
