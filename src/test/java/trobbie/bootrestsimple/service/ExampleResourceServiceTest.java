/*
 * Copyright (c) 2019 Trevor Robbie
 * This program is made available under the terms of the MIT License.
 */
/**
 *
 */
package trobbie.bootrestsimple.service;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import trobbie.bootrestsimple.dao.ExampleResourceTestDatabase;
import trobbie.bootrestsimple.dao.ResourceRepository;
import trobbie.bootrestsimple.dao.TestDatabase;
import trobbie.bootrestsimple.model.ExampleResource;
import trobbie.bootrestsimple.service.DefaultResourceService;

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

	@Before
	public void setup() {
		testDatabase.resetData();
	}

	@Test
	public void getResources_NoEntriesInRepo_ReturnEmptyList() {
		Mockito.when(resourceRepository.findAll())
		.thenReturn(testDatabase.getEmptyResources());

		Iterable<ExampleResource> result = resourceService.getResources();

		Assert.assertEquals(result.spliterator().getExactSizeIfKnown(), 0L);
	}

	@Test
	public void getResources_SomeEntriesInRepo_ReturnNonEmptyList() {
		Iterable<ExampleResource> testResources = testDatabase.getResources();
		Mockito.when(resourceRepository.findAll())
		.thenReturn(testResources);

		Iterable<ExampleResource> result = resourceService.getResources();
		long resultSize = result.spliterator().getExactSizeIfKnown();

		Assert.assertNotNull(result);
		Assert.assertNotEquals(resultSize, 0L);
		Assert.assertEquals(resultSize, testResources.spliterator().getExactSizeIfKnown());
	}

	@Test
	public void getResource_IdFound_ReturnResource() {
		Integer indexTest = 2;
		Long idTest = testDatabase.getResource(indexTest).getId();

		Mockito.when(resourceRepository.findById(idTest))
		.thenReturn(Optional.of(testDatabase.getResource(indexTest)));

		Optional<ExampleResource> result = resourceService.getResource(idTest.toString());

		Assert.assertNotNull(result.get());
		Assert.assertEquals(result.get().getId(), idTest);
	}



}
