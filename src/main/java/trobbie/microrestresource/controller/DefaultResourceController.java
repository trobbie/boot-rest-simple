package trobbie.microrestresource.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
	public ResponseEntity<T> upsertResource(@PathVariable("id") String id, @RequestBody T givenResource) {

		// The givenResource param must be converted from JSON.  Thanks to Spring’s HTTP
		// message converter support, you don’t need to do this conversion manually. Because Jackson 2
		// is on the classpath, Spring’s MappingJackson2HttpMessageConverter is automatically chosen to
		// convert the Resource instance to/from JSON.

		// Ensure the id from URI is same as that in request body
		if (!id.equals(givenResource.getId().toString())) {
			return new ResponseEntity<T>(HttpStatus.BAD_REQUEST);
		}

		boolean isUpdating = resourceService.getResource(id).isPresent();

		Optional<T> r = resourceService.saveResource(givenResource);
		if (r.isPresent()) {
			return new ResponseEntity<T>(r.get(), isUpdating ? HttpStatus.OK : HttpStatus.CREATED);
		} else {
			// something about client request could not allow it to be saved
			return new ResponseEntity<T>(HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	@RequestMapping(value=RELATIVE_PATH, method=RequestMethod.POST)
	public ResponseEntity<T> insertResource(@RequestBody T givenResource) {
		ResponseEntity<T> response;

		if (givenResource.getId() != null) {
			return new ResponseEntity<T>(HttpStatus.BAD_REQUEST);
		}

		Optional<T> r = resourceService.createResource(givenResource);
		if (r.isPresent()) {
			HttpHeaders headers = new HttpHeaders();
			headers.add("Location", RELATIVE_PATH+"/"+r.get().getId().toString());

			response = new ResponseEntity<T>(r.get(), headers, HttpStatus.CREATED);

		} else {
			// something about client request could not allow it to be saved
			response = new ResponseEntity<T>(HttpStatus.BAD_REQUEST);
		}
		return response;
	}

	@Override
	@RequestMapping(value=RELATIVE_PATH+"/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<T> deleteResource(@PathVariable("id") String id) {

		try {
			Optional<T> r = resourceService.getResource(id);
			if (r.isPresent())
				return new ResponseEntity<T>(r.get(), HttpStatus.NO_CONTENT);
			else
				return new ResponseEntity<T>(HttpStatus.NOT_FOUND);
		} catch(Exception e) {
			return new ResponseEntity<T>(HttpStatus.BAD_REQUEST);
		}
	}



}
