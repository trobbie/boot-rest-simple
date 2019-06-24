package trobbie.bootrestsimple.controller;

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

import trobbie.bootrestsimple.model.Resource;
import trobbie.bootrestsimple.service.ResourceService;
import trobbie.bootrestsimple.service.ResourceService.ReplaceResourceResult;

@RestController
public class DefaultResourceController<T extends Resource<ID>, ID> implements ResourceController<T, ID> {

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

		Optional<T> r = resourceService.getResource(id);

		// null response indicate something is wrong with the request
		if (r == null) return new ResponseEntity<T>(HttpStatus.BAD_REQUEST);

		return r.map(resource -> new ResponseEntity<T>(resource, HttpStatus.OK)
				).orElseGet(() -> new ResponseEntity<T>(HttpStatus.NOT_FOUND));
	}

	@Override
	@RequestMapping(value=RELATIVE_PATH+"/{id}", method=RequestMethod.PUT)
	public ResponseEntity<T> replaceResource(@PathVariable("id") String id, @RequestBody T givenResource) {

		// The givenResource param must be converted from JSON.  Thanks to Spring’s HTTP
		// message converter support, you don’t need to do this conversion manually. Because Jackson 2
		// is on the classpath, Spring’s MappingJackson2HttpMessageConverter is automatically chosen to
		// convert the Resource instance to/from JSON.

		Optional<ReplaceResourceResult<T>> r = resourceService.replaceResource(id, givenResource);

		return r.map(result -> {
			if (result.getInvalidArgsMessage() != null)
				return new ResponseEntity<T>(HttpStatus.BAD_REQUEST);
			else

				if (result.getSavedAsNewResource()) {
					HttpHeaders headers = new HttpHeaders();
					headers.add("Location", RELATIVE_PATH+"/"+result.getReplacedResource().getId().toString());

					return new ResponseEntity<T>(
							result.getReplacedResource(),
							headers,
							HttpStatus.CREATED);
				} else {
					return new ResponseEntity<T>(
							result.getReplacedResource(),
							HttpStatus.OK);
				}
		}).orElseGet(() -> {
			// something about client request could not allow it to be saved
			return new ResponseEntity<T>(HttpStatus.INTERNAL_SERVER_ERROR);
		});
	}

	@Override
	@RequestMapping(value=RELATIVE_PATH, method=RequestMethod.POST)
	public ResponseEntity<T> insertResource(@RequestBody T givenResource) {

		if (givenResource.getId() != null) {
			return new ResponseEntity<T>(HttpStatus.BAD_REQUEST);
		}

		Optional<T> r = resourceService.insertResource(givenResource);
		return r.map(resource -> {
			// ensure the service assigned an id (i.e. got saved)
			if (resource.getId() == null)
				return new ResponseEntity<T>(HttpStatus.INTERNAL_SERVER_ERROR);

			HttpHeaders headers = new HttpHeaders();
			headers.add("Location", RELATIVE_PATH+"/"+resource.getId().toString());

			return new ResponseEntity<T>(resource, headers, HttpStatus.CREATED);

		}).orElseGet(() -> {
			// something about client request could not allow it to be saved
			return new ResponseEntity<T>(HttpStatus.INTERNAL_SERVER_ERROR);
		});
	}

	@Override
	@RequestMapping(value=RELATIVE_PATH+"/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<T> deleteResource(@PathVariable("id") String id) {

		Optional<T> r = resourceService.getResource(id);

		// null response indicate something is wrong with the request
		if (r == null) return new ResponseEntity<T>(HttpStatus.BAD_REQUEST);

		return r.map(resource -> new ResponseEntity<T>(resource, HttpStatus.NO_CONTENT)
				).orElseGet(() -> new ResponseEntity<T>(HttpStatus.NOT_FOUND));

	}



}
