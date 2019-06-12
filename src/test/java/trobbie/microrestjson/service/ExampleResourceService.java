/*
 * Copyright (c) 2019 Trevor Robbie
 * This program is made available under the terms of the MIT License.
 */
/**
 *
 */
package trobbie.microrestjson.service;

import trobbie.microrestjson.model.ExampleResource;

/**
 * Used only for testing purposes to test an example implementation of DefaultResourceService
 *
 * @author Trevor Robbie
 *
 */
public class ExampleResourceService extends DefaultResourceService<ExampleResource, Long> {
	@Override
	public Long stringToIDConverter(String idString) {
		try {
			return Long.parseLong(idString);
		} catch (NumberFormatException e) {
			return null;
		}

	}
}
