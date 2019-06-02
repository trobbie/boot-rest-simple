package trobbie.microrestresource.controller;

import java.util.Optional;

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

@RestController
public class DefaultResourceController<T extends Resource, ID> implements ResourceController<T, ID> {

	public static final String RELATIVE_PATH = "/v1/resources";  // TODO: move out of this class?

	@Autowired // mark as being injected (wired by type)
	private ResourceService<T, ID> resourceService;

	@Override
	@RequestMapping(value=RELATIVE_PATH, method=RequestMethod.GET)
	public ResponseEntity<Iterable<T>> getResources() {
		return new ResponseEntity<Iterable<T>>(resourceService.getResources(), HttpStatus.OK);
	}

	@Override
	@RequestMapping(value=RELATIVE_PATH+"/{id}", method=RequestMethod.GET)
	public ResponseEntity<T> getResource(@PathVariable("id") String id) {


		try {
			Optional<T> r = resourceService.getResource(id);
			if (r.isPresent())
				return new ResponseEntity<T>(r.get(), HttpStatus.OK);
			else
				return new ResponseEntity<T>(HttpStatus.NOT_FOUND);
		} catch(Exception e) {
			return new ResponseEntity<T>(HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	@RequestMapping(value=RELATIVE_PATH+"/{id}", method=RequestMethod.PUT)
	public ResponseEntity<T> replaceResource(@PathVariable("id") String id, @RequestBody T givenResource) {

		// The givenResource param must be converted from JSON.  Thanks to Spring’s HTTP
		// message converter support, you don’t need to do this conversion manually. Because Jackson 2
		// is on the classpath, Spring’s MappingJackson2HttpMessageConverter is automatically chosen to
		// convert the Resource instance to/from JSON.

		// Ensure the id from URI is same as that in request body
		if (!id.equals(givenResource.getId().toString())) {
			return new ResponseEntity<T>(HttpStatus.BAD_REQUEST);
		}

		Optional<T> r = resourceService.replaceResource(givenResource);
		if (r.isPresent()) {
			return new ResponseEntity<T>(r.get(), HttpStatus.OK);
		} else {
			// since client used invalid id for a replace request, the request itself is considered bad,
			// i.e. not simply a 404 Not Found.
			return new ResponseEntity<T>(HttpStatus.BAD_REQUEST);
		}

	}

}
