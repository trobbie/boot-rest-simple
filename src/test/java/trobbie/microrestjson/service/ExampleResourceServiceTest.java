/*
 * Copyright (c) 2019 Trevor Robbie
 * This program is made available under the terms of the MIT License.
 */
/**
 *
 */
package trobbie.microrestjson.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import trobbie.microrestjson.dao.ExampleResourceTestDatabase;
import trobbie.microrestjson.dao.ResourceRepository;
import trobbie.microrestjson.dao.TestDatabase;
import trobbie.microrestjson.model.ExampleResource;

/**
 * Test ExampleResourceService implementation of the DefaultResourceService interface, that
 * simply uses ExampleResource as its resource type.
 *
 * Test method syntax: MethodName_StateUnderTest_ExpectedBehavior
 *
 * @author Trevor Robbie
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ExampleResourceService.class })
public class ExampleResourceServiceTest {

	@Autowired
	private DefaultResourceService<ExampleResource, Long> resourceService;

	@MockBean
	private ResourceRepository<ExampleResource, Long> resourceRepository;

	private TestDatabase<ExampleResource, Long> testDatabase = new ExampleResourceTestDatabase();;

	@Test
	public void getResources_NoEntriesInRepo_ReturnEmptyList() {
		Mockito.when(resourceRepository.findAll())
		.thenReturn(testDatabase.getEmptyResources());

		Iterable<ExampleResource> result = resourceService.getResources();

		Assert.assertEquals(result.spliterator().getExactSizeIfKnown(), 0L);
	}

	@Test
	public void getResources_SomeEntriesInRepo_ReturnNonEmptyList() {
		Mockito.when(resourceRepository.findAll())
		.thenReturn(testDatabase.getResources());

		Iterable<ExampleResource> result = resourceService.getResources();

		Assert.assertEquals(result.spliterator().getExactSizeIfKnown(), 1L);
	}



}
