package trobbie.bootrestsimple.controller;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
		return ResponseEntity.ok(resourceService.getResources());
	}

	@Override
	@RequestMapping(value=RELATIVE_PATH+"/{id}", method=RequestMethod.GET)
	public ResponseEntity<T> getResource(@PathVariable("id") String id) {

		Optional<T> r = resourceService.getResource(id);

		// null response indicate something is wrong with the request
		if (r == null) return ResponseEntity.badRequest().build();

		return r.map(resource -> ResponseEntity.ok(resource)
				).orElseGet(() -> ResponseEntity.notFound().build());
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
			if (result.getInvalidArgsMessage() != null) {
				return ResponseEntity.badRequest().<T>build();
			} else {
				if (result.getSavedAsNewResource()) {
					URI location = ServletUriComponentsBuilder.fromCurrentRequest().path(
							"/{id}").buildAndExpand(result.getReplacedResource().getId().toString()).toUri();
					return ResponseEntity.created(location).body(result.getReplacedResource());

				} else {
					return ResponseEntity.ok(result.getReplacedResource());
				}
			}
		}).orElseGet(() -> {
			// something about client request could not allow it to be saved
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).<T>build();
		});
	}

	@Override
	@RequestMapping(value=RELATIVE_PATH, method=RequestMethod.POST)
	public ResponseEntity<T> insertResource(@RequestBody T givenResource) {

		if (givenResource.getId() != null) {
			return ResponseEntity.badRequest().build();
		}

		Optional<T> r = resourceService.insertResource(givenResource);
		return r.map(resource -> {
			// ensure the service assigned an id (i.e. got saved)
			if (resource.getId() == null)
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).<T>build();

			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path(
					"/{id}").buildAndExpand(resource.getId().toString()).toUri();
			return ResponseEntity.created(location).body(resource);
		}).orElseGet(() -> {
			// something about client request could not allow it to be saved
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).<T>build();
		});
	}

	@Override
	@RequestMapping(value=RELATIVE_PATH+"/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<T> deleteResource(@PathVariable("id") String id) {

		Optional<T> r = resourceService.getResource(id);

		// null response indicate something is wrong with the request
		if (r == null) return ResponseEntity.badRequest().build();

		return r.map(resource -> ResponseEntity.noContent().<T>build()
				).orElseGet(() -> ResponseEntity.notFound().<T>build());

	}



}
