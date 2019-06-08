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
	 * Important: if cannot convert, must return null and not an runtime exception
	 *
	 * @param idString the resource id string from URI path
	 * @return 	the id with type ID.  If conversion not possible, must return null.
	 */
	public ID stringToIDConverter(String idString);

	/**
	 * Returns iterable of resources. Returns empty list if no resources.
	 */
	public Iterable<T> getResources();

	/**
	 * Returns an {@code Optional} describing a resource object, given the id of the resource.
	 *
	 * @param idString the id of the resource as string
	 * @return 	an {@code Optional} with value of the retrieved resource object; if not found,
	 * 			returns an empty {@code Optional}.  If id conversion fails, return null.
	 */
	public Optional<T> getResource(String idString);

	/**
	 * Saves the resource object at the specified resource's id, regardless of whether id exists already
	 * or not.
	 *
	 * <p> Note: this method must be idempotent.
	 * <p> Note: if only certain fields are to be updated (e.g. HTTP method: PATCH), use another method.
	 *
	 * @param specifiedResource the resource containing the new values
	 * @return 	an {@code Optional} of the replaced resource object; if resource id could not be saved, returns
	 * 			an empty {@code Optional}.
	 */
	public Optional<T> saveResource(T specifiedResource);

	/**
	 * Creates the resource object, which should have {@code id = null}.  On success, return the resource
	 * with the id assigned, along with any other calculated fields.
	 *
	 * @param specifiedResource the resource containing the new values, where {@code id = null}
	 * @return 	an {@code Optional} of the created resource object; if resource could not be created,
	 * 			returns an empty {@code Optional}
	 */
	public Optional<T> createResource(T specifiedResource);

	/**
	 * Deletes the resource given the id of the resource.  Returns true if successfully deleted.
	 *
	 * @param idString the id of the resource as string
	 * @return 	true if resource was successfully deleted.  If id conversion fails, return null.
	 */
	public Boolean deleteResource(String idString);

}
