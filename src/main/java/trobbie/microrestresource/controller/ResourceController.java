package trobbie.microrestresource.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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

	/**
	 * Returns list of retrieved resources.
	 *
	 * @return the HTTP representation of the list of retrieved resources
	 */
	@RequestMapping(value=RELATIVE_PATH, method=RequestMethod.GET)
	public ResponseEntity<List<Resource>> getResources() {
		return new ResponseEntity<List<Resource>>(resourceService.getResources(), HttpStatus.OK);
	}

	/**
	 * Returns the retrieved resource.
	 *
	 * @param id the id of the specified resource
	 * @return the HTTP representation of the retrieved resource, identified by the specified id, or an HTTP
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

	/**
	 * Replaces the given resource, given the resource in the request body.  If resource id is not found,
	 * return 400 Bad Request.
	 *
	 * @param id      the id of the entity to update
	 * @param entity  the entity values with which to update. Entity's ID must match id of path variable.
	 * @return the HTTP representation of the resource. If specified id was not found, return response code
	 * 		of BadRequest.  If specified id does not match givenResource's id, return response code of
	 * 		BadRequest.
	 */
	@RequestMapping(value=RELATIVE_PATH+"/{id}", method=RequestMethod.PUT)
	public ResponseEntity<Resource> replaceResource(@PathVariable("id") Long id, @RequestBody Resource givenResource) {

		// The givenResource param must be converted from JSON.  Thanks to Spring’s HTTP
		// message converter support, you don’t need to do this conversion manually. Because Jackson 2
		// is on the classpath, Spring’s MappingJackson2HttpMessageConverter is automatically chosen to
		// convert the Resource instance to/from JSON.

		// important to ensure the id from URI is same as that in request body
		if (id != givenResource.getId()) {
			return new ResponseEntity<Resource>(HttpStatus.BAD_REQUEST);
		}

		Resource r = resourceService.replaceResource(givenResource);
		if (r == null) {
			// since client used invalid id for a replace request, the request itself is considered bad,
			// i.e. not simply a 404 Not Found.
			return new ResponseEntity<Resource>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Resource>(r, HttpStatus.OK);
	}

}
