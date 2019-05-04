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

	public List<Resource> getResources() {
		return allResources;
	}

	public Resource getResource(Long id) {
		for (Resource r : allResources) {
			if (r.getId() == id) {
				return r;
			}
		}
		return null;
	}
}
