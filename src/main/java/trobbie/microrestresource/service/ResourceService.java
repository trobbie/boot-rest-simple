package trobbie.microrestresource.service;

import java.util.Optional;

import trobbie.microrestresource.model.Resource;

/**
 * Interface for providing service to ultimately interact resource data.
 *
 * @author Trevor Robbie
 *
 */
public interface ResourceService<T extends Resource, ID> {

	/**
	 * Converter function from String (resource id from URI path) to ID's type.
	 *
	 * @param idString the resource id string from URI path
	 * @return the id with type ID
	 */
	public ID stringToIDConverter(String idString);

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
	public Optional<T> getResource(String id);

	/**
	 * Replaces the resource object at the specified resource's id.  If resource's id does not already
	 * exist, then return an empty {@code Optional}.
	 *
	 * <p> Note: this method must be idempotent.
	 * <p> Note: if only certain fields are to be updated (e.g. HTTP method: PATCH), use another method.
	 *
	 * @param specifiedResource the resource containing the new values
	 * @return an {@code Optional} of the replaced resource object; if resource id was not found, return
	 * an empty {@code Optional}
	 */
	public Optional<T> replaceResource(T specifiedResource);

}
