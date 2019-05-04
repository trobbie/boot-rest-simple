package trobbie.microrestresource.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import trobbie.microrestresource.model.Resource;
import trobbie.microrestresource.service.ResourceService;

/**
 * @author Trevor Robbie
 *
 */

@RestController
public class ResourceController {
	static public final String RELATIVE_PATH = "/resources";
	@Autowired
	private ResourceService resourceService;

	/*
	 * Returns list of retrieved resources.
	 *
	 * @return the HTTP response of a list of retrieved resources
	 */
	@RequestMapping(value=RELATIVE_PATH, method=RequestMethod.GET)
	public ResponseEntity<List<Resource>> getResources() {
		return new ResponseEntity<List<Resource>>(resourceService.getResources(), HttpStatus.OK);
	}

	/*
	 * Returns the retrieved resource.
	 *
	 * @param id the id of the specified resource
	 * @return the HTTP response of a retrieved resource, identified by the specified id, or an HTTP
	 * 		response of status code 404 if not found.
	 */
	@RequestMapping(value=RELATIVE_PATH+"/{id}", method=RequestMethod.GET)
	public ResponseEntity<Resource> getResource(@PathVariable("id") Long id) {
		Resource r = resourceService.getResource(id);
		if (r == null) {
			return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Resource>(r, HttpStatus.OK);
	}

}
