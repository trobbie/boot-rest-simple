package trobbie.microrestresource.service;

import java.util.Optional;

import org.springframework.stereotype.Component;

import trobbie.microrestresource.model.Resource;

/**
 * Interface for providing service to ultimately interact resource data.
 *
 * @author Trevor Robbie
 *
 */
@Component
public interface ResourceService<T extends Resource, ID> {

	/**
	 * Returns iterable of resources. Returns empty list if no resources.
	 */
	public Iterable<T> getResources();

	/**
	 * Returns an {@code Optional} describing a resource object, given the id of the resource.
	 *
	 * @param id the id of the resource
	 * @return an {@code Optional} with value of the retrieved resource object; if not found, returns
	 * an empty {@code Optional}
	 */
	public Optional<T> getResource(ID id);

	/**
	 * Replaces the resource object at the specified resource's id.  If resource's id does not already
	 * exist, then return null.
	 *
	 * <p> Note: this method must be idempotent.
	 * <p> Note: if only certain fields are to be updated (e.g. HTTP method: PATCH), use another method.
	 *
	 * @param specifiedResource the resource containing the new values
	 * @return the replaced resource object, or null if id could not be found
	 */
	public T replaceResource(T specifiedResource);

}
