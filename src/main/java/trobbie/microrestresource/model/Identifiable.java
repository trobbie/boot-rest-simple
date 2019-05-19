/*
 * Copyright (c) 2019 Trevor Robbie
 * This program is made available under the terms of the MIT License.
 */
/**
 *
 */
package trobbie.microrestresource.model;

/**
 * @author Trevor Robbie
 *
 */
public interface Identifiable<ID> {
	public ID getId();
	public void setId(ID id);

}
