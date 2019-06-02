package trobbie.microrestresource.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import trobbie.microrestresource.model.Resource;

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
	 * @param id the id of the specified resource
	 * @return the HTTP representation of the retrieved resource, identified by the specified id, or an HTTP
	 * 		response of status code 404 if not found.
	 */
	public ResponseEntity<T> getResource(@PathVariable("id") String id);

	/**
	 * Replaces the given resource, given the resource in the request body.  If resource id is not found,
	 * return 400 Bad Request.  If resource_id is not a valid id type, return 404 Not Found.
	 *
	 * This returns the resource that was just updated, containing values given from the repository.
	 * Debatably, we could return 204 (No Content), but this interface allows certain fields to be
	 * calculated server-side (e.g. an exact dateCreated, dateModified, etc.).
	 *
	 * @param id      the id of the entity to update
	 * @param entity  the entity values with which to update. Entity's ID must match id of path variable.
	 * @return the HTTP representation of the resource. If specified id was not found, return response code
	 * 		of BadRequest.  If specified id does not match givenResource's id, return response code of
	 * 		BadRequest.
	 */
	public ResponseEntity<T> replaceResource(@PathVariable("id") String id, @RequestBody T givenResource);

}
