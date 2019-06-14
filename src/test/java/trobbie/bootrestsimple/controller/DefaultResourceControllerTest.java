/**
 *
 */
package trobbie.bootrestsimple.controller;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import trobbie.bootrestsimple.controller.DefaultResourceController;
import trobbie.bootrestsimple.controller.ResourceController;
import trobbie.bootrestsimple.dao.ExampleResourceTestDatabase;
import trobbie.bootrestsimple.dao.TestDatabase;
import trobbie.bootrestsimple.model.ExampleResource;
import trobbie.bootrestsimple.model.Resource;
import trobbie.bootrestsimple.service.DefaultResourceService;


/**
 * Test DefaultResourceController implementation of the ResourceController interface.
 * An example resource model is needed to run the tests.
 *
 * Test method syntax: MethodName_StateUnderTest_ExpectedBehavior
 *
 * @author Trevor Robbie
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = ResourceController.class)
public class DefaultResourceControllerTest {
	public static final Class<ExampleResource> RESOURCE_CLASS = ExampleResource.class;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private DefaultResourceService<ExampleResource, Long> resourceService;

	private TestDatabase<ExampleResource, Long> testDatabase = new ExampleResourceTestDatabase();

	private static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static Resource asResource(final String jsonSource) {
		try {
			return new ObjectMapper().readValue(jsonSource, RESOURCE_CLASS);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Before
	public void resetData() {
		this.testDatabase.resetData();
	}

	@Test
	public void getResources_RequestedOnEmptyData_Return200AndEmptyList() throws Exception {

		Mockito.when(resourceService.getResources())
		.thenReturn(this.testDatabase.getEmptyResources());

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get(DefaultResourceController.RELATIVE_PATH)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8");

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		String expected = "[]";
		Assert.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}

	@Test
	public void getResources_RequestedOnNonEmptyData_Return200WithList() throws Exception {

		Mockito.when(resourceService.getResources())
		.thenReturn(this.testDatabase.getResources());

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get(DefaultResourceController.RELATIVE_PATH)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8");

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		String expected = "[{id:1,name:MockResource1},{id:2,name:MockResource2}]";
		Assert.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}

	@Test
	public void getResource_IdFound_ReturnResource() throws Exception {

		Mockito.when(resourceService.getResource(Mockito.any()))
		.thenReturn(Optional.of(this.testDatabase.getResource(1)));

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get(DefaultResourceController.RELATIVE_PATH + "/" + this.testDatabase.getResource(1).getId())
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8");

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		String expected = asJsonString(this.testDatabase.getResource(1));
		Assert.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}

	@Test
	public void getResource_IdUnknown_Return404NotFound() throws Exception {

		Long unknownId = 99999L;
		Mockito.when(resourceService.getResource(Mockito.any()))
		.thenReturn(Optional.empty());

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get(DefaultResourceController.RELATIVE_PATH + "/" + unknownId)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8");

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		Assert.assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
	}

	@Test
	public void getResource_StringIdRequested_Return400BadRequest() throws Exception {

		Mockito.when(resourceService.getResource(Mockito.any()))
		.thenReturn(null);

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get(DefaultResourceController.RELATIVE_PATH + "/stringtest")
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8");

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
	}

	@Test
	public void getResource_HeadRequest_ActiveAndNoBody() throws Exception {

		Mockito.when(resourceService.getResource(Mockito.any()))
		.thenReturn(Optional.of(this.testDatabase.getResource(1)));

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.head(DefaultResourceController.RELATIVE_PATH + "/" + this.testDatabase.getResource(1).getId())
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8");
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		// Spring 4.3+ has implicit support for HEAD. There is no need to test their implementation.
		// Here we are testing if it is active for our API.

		Assert.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
		Assert.assertEquals("Should not return message-body in response", 0, result.getResponse().getContentAsString().length());
	}

	@Test
	public void upsertResource_Requested_ReturnSameResource() throws Exception {

		Resource mockResource2 = this.testDatabase.changeResource(2);

		Mockito.when(resourceService.getResource(this.testDatabase.getResource(2).getId().toString()))
		.thenReturn(Optional.of(this.testDatabase.getResource(2)));
		Mockito.when(resourceService.saveResource(ArgumentMatchers.any(RESOURCE_CLASS)))
		.thenReturn(Optional.of(this.testDatabase.getResource(2)));

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.put(DefaultResourceController.RELATIVE_PATH + "/" + this.testDatabase.getResource(2).getId())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(asJsonString(mockResource2));

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		String expected = asJsonString(mockResource2);
		Assert.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}

	@Test
	public void upsertResource_IdMismatch_Return400BadRequest() throws Exception {

		// should not call service layer at all
		Mockito.when(resourceService.getResource(ArgumentMatchers.any()))
		.thenThrow(new RuntimeException());
		Mockito.when(resourceService.saveResource(ArgumentMatchers.any()))
		.thenThrow(new RuntimeException());

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.put(DefaultResourceController.RELATIVE_PATH + "/" + this.testDatabase.getResource(1).getId())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(asJsonString(this.testDatabase.getResource(2)));

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
	}

	@Test
	public void upsertResource_RequestWithEmptyBody_Return400BadRequest() throws Exception {

		// should not call service layer at all
		Mockito.when(resourceService.getResource(ArgumentMatchers.any()))
		.thenThrow(new RuntimeException());
		Mockito.when(resourceService.saveResource(ArgumentMatchers.any()))
		.thenThrow(new RuntimeException());

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.put(DefaultResourceController.RELATIVE_PATH + "/" + this.testDatabase.getResource(2).getId())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content("");

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
	}

	@Test
	public void upsertResource_RequestWithInvalidBody_Return400BadRequest() throws Exception {

		// should not call service layer at all
		Mockito.when(resourceService.getResource(ArgumentMatchers.any()))
		.thenThrow(new RuntimeException());
		Mockito.when(resourceService.saveResource(ArgumentMatchers.any()))
		.thenThrow(new RuntimeException());

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.put(DefaultResourceController.RELATIVE_PATH + "/" + this.testDatabase.getResource(2).getId())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content("[_id='invalidentity'");

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
	}

	@Test
	public void upsertResource_RequestNewResource_Return201Created() throws Exception {

		Mockito.when(resourceService.getResource(this.testDatabase.getResource(2).getId().toString()))
		.thenReturn(Optional.empty());
		Mockito.when(resourceService.saveResource(ArgumentMatchers.any(RESOURCE_CLASS)))
		.thenReturn(Optional.of(this.testDatabase.getResource(2)));

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.put(DefaultResourceController.RELATIVE_PATH + "/" + this.testDatabase.getResource(2).getId())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(asJsonString(this.testDatabase.getResource(2)));

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		Assert.assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());
	}

	@Test
	public void insertResource_RequestNewResource_Return201Created() throws Exception {

		Integer new_index = this.testDatabase.saveResource(this.testDatabase.newUnsavedResource());

		Mockito.when(resourceService.createResource(ArgumentMatchers.any(RESOURCE_CLASS)))
		.thenReturn(Optional.of(this.testDatabase.getResource(new_index)));

		// temporarily assign id=null to get the JSON body for the request
		Resource new_resource = this.testDatabase.getResource(new_index);
		Object new_id = new_resource.getId();
		new_resource.setId(null);
		String jsonResourceWithNullId = asJsonString(new_resource);
		new_resource.setId(new_id); // reassigned

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post(DefaultResourceController.RELATIVE_PATH)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(jsonResourceWithNullId);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		Assert.assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());
		Resource resource_result = asResource(result.getResponse().getContentAsString());

		Assert.assertEquals("Id expected to be assigned", resource_result.getId(), new_resource.getId());
	}

	@Test
	public void insertResource_RequestResourceIdAssigned_Return400BadRequest() throws Exception {

		Mockito.when(resourceService.createResource(ArgumentMatchers.any()))
		.thenThrow(new RuntimeException());

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post(DefaultResourceController.RELATIVE_PATH)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(asJsonString(this.testDatabase.getResource(1)));

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		Assert.assertEquals("Expected 400 response when resource id already specified", HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
	}

	@Test
	public void insertResource_RequestUsingUnsupportedMediaType_Return415() throws Exception {

		Mockito.when(resourceService.createResource(ArgumentMatchers.any()))
		.thenThrow(new RuntimeException());

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post(DefaultResourceController.RELATIVE_PATH)
				.contentType(MediaType.APPLICATION_XML)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content("<unsupported></unsupported>");

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		Assert.assertEquals("Expected 415 response when sending XML in request body", HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), result.getResponse().getStatus());
	}

	@Test
	public void deleteResource_ExistingResource_Return204() throws Exception {

		Mockito.when(resourceService.getResource(this.testDatabase.getResource(1).getId().toString()))
		.thenReturn(Optional.of(this.testDatabase.getResource(1)));
		Mockito.when(resourceService.deleteResource(this.testDatabase.getResource(1).getId().toString()))
		.thenReturn(Boolean.TRUE);

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.delete(DefaultResourceController.RELATIVE_PATH + "/" + this.testDatabase.getResource(1).getId())
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8");

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		Assert.assertEquals("Expected 204 No Content when deleting existing resource",
				HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus());
	}

	@Test
	public void deleteResource_NonExistingResource_Return404() throws Exception {

		Resource res = this.testDatabase.newUnsavedResource();
		res.setId(this.testDatabase.getIdNeverExist());

		Mockito.when(resourceService.getResource(res.getId().toString()))
		.thenReturn(Optional.empty());
		Mockito.when(resourceService.deleteResource(ArgumentMatchers.any()))
		.thenThrow(new RuntimeException());

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.delete(DefaultResourceController.RELATIVE_PATH + "/" + res.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8");

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		Assert.assertEquals("Expected 404 Not Found when deleting non-existing resource",
				HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
	}

}
