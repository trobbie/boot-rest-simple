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
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import trobbie.bootrestsimple.config.UnitTestConfig;
import trobbie.bootrestsimple.dao.ResourceRepository;
import trobbie.bootrestsimple.dao.TestDatabase;
import trobbie.bootrestsimple.model.ExampleResource;

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
@ActiveProfiles("test")
@ContextConfiguration(classes = { UnitTestConfig.class } )
// @SpringBootTest()
// @ContextConfiguration(classes = {ExampleResourceService.class })
public class ExampleResourceServiceTest {

	@Autowired
	private DefaultResourceService<ExampleResource, Long> resourceService;

	@MockBean
	private ResourceRepository<ExampleResource, Long> resourceRepository;

	@Autowired
	private TestDatabase<ExampleResource, Long> testDatabase;

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

	@Test
	public void getResource_BadIdConversion_ReturnEmptyOptional() {
		Optional<ExampleResource> result = resourceService.getResource("unconvertable_id");

		Assert.assertEquals(false, result.isPresent());
	}

	@Test
	public void replaceResource_IdFound_ReturnResource() {
		Integer indexTest = 2;
		ExampleResource res = testDatabase.getResource(indexTest);

		Mockito.when(resourceRepository.findById(ArgumentMatchers.any()))
		.thenReturn(Optional.of(testDatabase.getResource(indexTest)));
		Mockito.when(resourceRepository.save(res))
		.thenReturn(res);

		Optional<ResourceService.ReplaceResourceResult<ExampleResource>> result
		= resourceService.replaceResource(res.getId().toString(), res );

		Assert.assertEquals(true, result.isPresent());
		Assert.assertEquals(false, result.get().getSavedAsNewResource());
	}

	@Test(expected = IllegalArgumentException.class)
	public void replaceResource_NullResource_ThrowException() {

		Optional<ResourceService.ReplaceResourceResult<ExampleResource>> result
		= resourceService.replaceResource("1", null );
	}

	@Test
	public void replaceResource_IdNotFound_ReturnResource() {
		Integer indexTest = 2;
		ExampleResource res = testDatabase.getResource(indexTest);

		Mockito.when(resourceRepository.findById(ArgumentMatchers.any()))
		.thenReturn(Optional.empty());
		Mockito.when(resourceRepository.save(res))
		.thenReturn(res);

		Optional<ResourceService.ReplaceResourceResult<ExampleResource>> result
		= resourceService.replaceResource(res.getId().toString(), res );

		Assert.assertEquals(true, result.isPresent());
		Assert.assertEquals(true, result.get().getSavedAsNewResource());
	}

	@Test
	public void replaceResource_ResourceIdNotAssigned_ReturnResourceWithIdAssigned() {
		Integer indexTest = 2;
		ExampleResource res = testDatabase.getResource(indexTest);
		Long idTest = res.getId();

		Mockito.when(resourceRepository.findById(ArgumentMatchers.any()))
		.thenReturn(Optional.empty());
		Mockito.when(resourceRepository.save(ArgumentMatchers.any()))
		.thenReturn(res);

		res.setId(null);

		Optional<ResourceService.ReplaceResourceResult<ExampleResource>> result
		= resourceService.replaceResource(idTest.toString(), res );

		Assert.assertEquals(true, result.isPresent());
		Assert.assertNotNull(result.get().getReplacedResource().getId());
	}

	@Test
	public void replaceResource_ErrorDuringSave_ReturnEmptyOptional() {
		Integer indexTest = 2;
		ExampleResource res = testDatabase.getResource(indexTest);

		Mockito.when(resourceRepository.findById(ArgumentMatchers.any()))
		.thenReturn(Optional.empty());
		Mockito.when(resourceRepository.save(res))
		.thenThrow(new RuntimeException());

		Optional<ResourceService.ReplaceResourceResult<ExampleResource>> result
		= resourceService.replaceResource(res.getId().toString(), res );

		Assert.assertEquals(false, result.isPresent());
	}

	@Test
	public void replaceResource_BadIdConversion_ReturnResultWithError() {
		Integer indexTest = 2;
		ExampleResource res = testDatabase.getResource(indexTest);

		Optional<ResourceService.ReplaceResourceResult<ExampleResource>> result
		= resourceService.replaceResource("invalid_typed_id", res);

		Assert.assertEquals(true, result.isPresent());
		Assert.assertNotNull(result.get().getInvalidArgsMessage());
	}

	@Test
	public void insertResource_NewResource_ReturnResourceWithId() {

		ExampleResource newResource = testDatabase.newUnsavedResource();

		Mockito.when(resourceRepository.save(newResource))
		.thenReturn(testDatabase.getResource(2)); // not testing resource other than the id field

		Optional<ExampleResource> result
		= resourceService.insertResource(newResource);

		Assert.assertEquals(true, result.isPresent());
		Assert.assertNotNull(result.get().getId());
	}

	@Test(expected = IllegalArgumentException.class)
	public void insertResource_NullResource_ThrowRuntime() {

		// should not be called
		Mockito.when(resourceRepository.save(ArgumentMatchers.any()))
		.thenThrow(new RuntimeException());

		resourceService.insertResource(null);

	}

	@Test(expected = IllegalArgumentException.class)
	public void insertResource_IdAlreadyAssigned_ThrowRuntime() {
		Integer indexTest = 2;
		ExampleResource res = testDatabase.getResource(indexTest);

		// should not be called
		Mockito.when(resourceRepository.save(ArgumentMatchers.any()))
		.thenThrow(new RuntimeException());

		resourceService.insertResource(res);

	}

	@Test
	public void insertResource_ErrorDuringSave_ReturnEmptyOptional() {
		ExampleResource newResource = testDatabase.newUnsavedResource();

		Mockito.when(resourceRepository.save(ArgumentMatchers.any()))
		.thenThrow(new RuntimeException());

		Optional<ExampleResource> result = resourceService.insertResource(newResource);

		Assert.assertEquals(false, result.isPresent());
	}

	@Test
	public void deleteResource_BadIdConversion_ReturnEmptyOptional() {
		Boolean result = resourceService.deleteResource("unconvertable_id");

		Assert.assertNull(result);
	}

	@Test
	public void deleteResource_ExistingId_ReturnTrue() {
		Integer indexTest = 2;
		ExampleResource res = testDatabase.getResource(indexTest);

		Mockito.doNothing().when(resourceRepository).delete(ArgumentMatchers.any());

		Boolean result = resourceService.deleteResource(res.getId().toString());

		Assert.assertEquals(true, result);
	}

}
