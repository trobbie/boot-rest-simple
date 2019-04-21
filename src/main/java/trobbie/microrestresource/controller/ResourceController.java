package trobbie.microrestresource.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import trobbie.microrestresource.model.Resource;

/**
 * @author Trevor Robbie
 *
 */

@RestController
public class ResourceController {

	@Autowired
	private trobbie.microrestresource.service.ResourceService resourceService;

	// Custom: replace "/resources" with the plural of the microservice's resource name
	@GetMapping("/resources")
	public List<Resource> getResources() {
		return resourceService.getResources();
	}

}
