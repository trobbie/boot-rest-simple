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

import trobbie.microrestresource.model.SimpleResource;
import trobbie.microrestresource.service.SimpleResourceService;

/**
 * @author Trevor Robbie
 *
 */

@RestController
public class SimpleResourceController implements ResourceController<SimpleResource, Long> {
	static public final String RELATIVE_PATH = "/resources";

	@Autowired  // mark as being injected (wired by type)
	private SimpleResourceService resourceService;

	@Override
	@RequestMapping(value=RELATIVE_PATH, method=RequestMethod.GET)
	public ResponseEntity<Iterable<SimpleResource>> getResources() {
		return new ResponseEntity<Iterable<SimpleResource>>(resourceService.getResources(), HttpStatus.OK);
	}

	@Override
	@RequestMapping(value=RELATIVE_PATH+"/{id}", method=RequestMethod.GET)
	public ResponseEntity<SimpleResource> getResource(@PathVariable("id") Long id) {
		Optional<SimpleResource> r = resourceService.getResource(id);
		if (r.isPresent())
			return new ResponseEntity<SimpleResource>(r.get(), HttpStatus.OK);
		else
			return new ResponseEntity<SimpleResource>(HttpStatus.NOT_FOUND);
	}

	@Override
	@RequestMapping(value=RELATIVE_PATH+"/{id}", method=RequestMethod.PUT)
	public ResponseEntity<SimpleResource> replaceResource(@PathVariable("id") Long id, @RequestBody SimpleResource givenResource) {

		// The givenResource param must be converted from JSON.  Thanks to Spring’s HTTP
		// message converter support, you don’t need to do this conversion manually. Because Jackson 2
		// is on the classpath, Spring’s MappingJackson2HttpMessageConverter is automatically chosen to
		// convert the Resource instance to/from JSON.

		// important to ensure the id from URI is same as that in request body
		if (id != givenResource.getId()) {
			return new ResponseEntity<SimpleResource>(HttpStatus.BAD_REQUEST);
		}

		SimpleResource r = resourceService.replaceResource(givenResource);
		if (r == null) {
			// since client used invalid id for a replace request, the request itself is considered bad,
			// i.e. not simply a 404 Not Found.
			return new ResponseEntity<SimpleResource>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<SimpleResource>(r, HttpStatus.OK);
	}

}
