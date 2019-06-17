package trobbie.bootrestsimple.service;

import java.util.Optional;

import trobbie.bootrestsimple.model.Resource;

/**
 * Interface for providing service to ultimately interact resource data.
 *
 * @author Trevor Robbie
 *
 */
public interface ResourceService<T extends Resource<ID>, ID> {

	public static class ReplaceResourceResult<T> {
		private T replacedResource;
		private Boolean savedAsNewResource = Boolean.FALSE;
		/**
		 * @return the replacedResource
		 */
		public T getReplacedResource() {
			return replacedResource;
		}
		/**
		 * @param replacedResource the replacedResource to set
		 */
		public void setReplacedResource(T replacedResource) {
			this.replacedResource = replacedResource;
		}
		/**
		 * @return the savedAsNewResource
		 */
		public Boolean getSavedAsNewResource() {
			return savedAsNewResource;
		}
		/**
		 * @param savedAsNewResource the savedAsNewResource to set
		 */
		public void setSavedAsNewResource(Boolean savedAsNewResource) {
			this.savedAsNewResource = savedAsNewResource;
		}

	}

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
	 * Returns an {@code Optional} describing a resource object, given the id of the resource.  If the id
	 * is not found, or the idString could not be converted, then return empty {@code Optional}.
	 *
	 * @param idString the id of the resource as string
	 * @return 	an {@code Optional} with value of the retrieved resource object; if not found,
	 * 			returns an empty {@code Optional}.  If id conversion fails, return empty {@code Optional}.
	 */
	public Optional<T> getResource(String idString);

	/**
	 * Replaces the resource object, identified by id.  If id does not already exist, it is effectively
	 * saved as new.  Whether the id exists or not, the specified resource's id field is assigned the
	 * value of parameter id.
	 *
	 * <p> Note: this method must be idempotent.
	 *
	 * @param id the id of the resource to replace, as a String
	 * @param specifiedResource the resource containing the new values; the id field is ignored
	 * @return 	an {@code Optional} of the replaced resource object; if resource id could not be saved,
	 * 			returns an empty {@code Optional}.
	 */
	public Optional<ReplaceResourceResult<T>> replaceResource(String id, T specifiedResource);

	/**
	 * Inserts the resource object, which should have {@code id = null}.  On success, return the resource
	 * with the id assigned, along with any other calculated fields.
	 *
	 * @param specifiedResource the resource containing the new values, where {@code id = null}
	 * @return 	an {@code Optional} of the inserted resource object; if resource could not be created,
	 * 			returns an empty {@code Optional}
	 */
	public Optional<T> insertResource(T specifiedResource);

	/**
	 * Deletes the resource given the id of the resource.  Returns true if successfully deleted.
	 *
	 * @param idString the id of the resource as string
	 * @return 	true if resource was successfully deleted.  If id conversion fails, return null.
	 */
	public Boolean deleteResource(String idString);

}
