package trobbie.microrestresource.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import trobbie.microrestresource.model.Resource;

/**
 *
 *
 * @author Trevor Robbie
 *
 */
@Component
public class ResourceService {

	private static List<Resource> allResources;

	static {
		// TODO: needs data layer implementation
		//   For now, hard-code the data
		allResources = new ArrayList<Resource>();
		allResources.add(new Resource(1, "TestUser1"));
	}

	/*
	 * Returns list of resources. Returns empty list if no resources.
	 */
	public List<Resource> getResources() {
		return allResources;
	}

	/*
	 * Returns resource object, given the id of the resource. Returns null if id not found.
	 *
	 * @param id the id of the resource
	 * @return the retrieved resource object, or null if not found
	 */
	public Resource getResource(Long id) {
		for (Resource r : allResources) {
			if (r.getId() == id) {
				return r;
			}
		}
		return null;
	}

	/*
	 * Replaces the resource object at the specified resource's id.  If resource id does not already
	 * exist, then return null.
	 *
	 * <p> Note: this method must be idempotent.
	 * <p> Note: if only certain fields are to be updated (e.g. HTTP method: PATCH), use another method.
	 *
	 * @param specifiedResource the resource containing the new values
	 * @return the replaced resource object, or null if id could not be found
	 */
	public Resource replaceResource(Resource specifiedResource) {

		Resource resourceToUpdate = getResource(specifiedResource.getId());
		if (resourceToUpdate == null) return null;

		resourceToUpdate.setName(specifiedResource.getName());

		return resourceToUpdate;

	}

}
