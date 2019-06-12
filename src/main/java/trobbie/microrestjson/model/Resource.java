package trobbie.microrestjson.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import trobbie.microrestjson.model.ExampleResource;

/**
 * Interface for the resource model.  Implement the ID and any additional fields.
 *
 * @author Trevor Robbie
 *
 */
@JsonDeserialize(as = ExampleResource.class)
public interface Resource<ID> {

	/**
	 * @return the id
	 */
	public ID getId();

	/**
	 * @param id the id to set
	 */
	public void setId(ID id);

}
