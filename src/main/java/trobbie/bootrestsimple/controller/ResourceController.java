package trobbie.bootrestsimple.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import trobbie.bootrestsimple.model.Resource;

/**
 * Interface for REST controller intended to forward web requests to ResourceService layer.
 *
 * The ID is always interpreted as a String from the Path.  The REST service layer defines a
 * converter to the resource's ID type when implementing the interface.
 *
 * Responses are sent in JSON format.
 *
 * @author Trevor Robbie
 *
 */

public interface ResourceController<T extends Resource, ID> {

	/**
	 * Returns list of retrieved resources.
	 *
	 * @return the HTTP representation of the list of retrieved resources
	 */
	public ResponseEntity<Iterable<T>> getResources();

	/**
	 * Returns the retrieved resource.  Representation is as a single resource, not in array form.
	 *
	 * @param id	the id of the specified resource
	 * @return the HTTP representation of the retrieved resource, identified by the specified id, or an HTTP
	 * 		response of status code 404 if not found.
	 */
	public ResponseEntity<T> getResource(@PathVariable("id") String id);

	/**
	 * Updates the given resource if already exists, or create new resource if not exists, given
	 * the resource in the request body.  Return 200 (OK) if updated, or 201 (Created) if inserted.
	 * If path's resource id does not match id of resource itself, return 400 (Bad Request).
	 *
	 * This returns the resource that was just updated, containing values given from the repository.
	 * Debatably, we could return 204 (No Content), but this interface allows certain fields to be
	 * calculated server-side (e.g. an exact dateCreated, dateModified, etc.) and allows client thus
	 * to be informed of those calculations.
	 *
	 * @param id      the id of the entity to update
	 * @param entity  the entity values with which to update/insert. Entity's ID must match id of path variable.
	 * @return the HTTP representation of the resource after having saved it. If specified id was not found,
	 * 		return response code of 201 (Created).  If specified id does not match givenResource's id, return
	 * 		response code of 400 (Bad Request).
	 */
	public ResponseEntity<T> upsertResource(@PathVariable("id") String id, @RequestBody T givenResource);


	/**
	 * Inserts the resource given in the request body.  Upon success, return 201 (Created) with the resource's new URI
	 * included in the Location header, and the resource (and any updated fields) in the response body.  Updated
	 * fields would include id, and could also include fields such as dateCreated and dateModified.
	 *
	 * @param entity  the entity values of the resource with which to insert.
	 * @return the HTTP representation of the resource after having saved it. On success, a 201 (Created) response code
	 * 		is used.
	 */
	public ResponseEntity<T> insertResource(@RequestBody T givenResource);

	/**
	 * Deletes the resource identified by the id given in the path.  Returns 204 (No Content) if the
	 * repository received the delete request of an existing resource.  Returns 404 (Not Found) if the
	 * resource id could not be found.
	 *
	 * @param id	the id of the specified resource to delete
	 * @return an Http Response with no message-body.  If specified id was not found, return response code
	 * 			404 (Not Found); if the id exists, return 204 (No Content) to indicate the delete request
	 * 			had been received by repository.
	 */
	public ResponseEntity<T> deleteResource(@PathVariable("id") String id);

}
