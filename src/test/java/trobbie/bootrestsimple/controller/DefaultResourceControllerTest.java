/**
 *
 */
package trobbie.bootrestsimple.controller;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import trobbie.bootrestsimple.dao.TestDatabase;
import trobbie.bootrestsimple.model.Resource;
import trobbie.bootrestsimple.service.DefaultResourceService;
import trobbie.bootrestsimple.service.ResourceService;


/**
 * Test DefaultResourceController implementation of the ResourceController interface.
 * An example resource model is needed to run the tests.
 *
 * Test method syntax: MethodName_StateUnderTest_ExpectedBehavior
 *
 * @author Trevor Robbie
 *
 */
public abstract class DefaultResourceControllerTest<T extends Resource<ID>, ID> {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private DefaultResourceService<T, ID> resourceService;

	@Autowired
	private TestDatabase<T, ID> testDatabase;

	private static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
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
	public void getResource_InvalidPathId_Return400BadRequest() throws Exception {

		Mockito.when(resourceService.getResource(Mockito.any()))
		.thenReturn(null);

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get(DefaultResourceController.RELATIVE_PATH + "/invalid_typed_id")  // change this if String id is actually ok
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
	public void replaceResource_ExistingResource_ReturnSameResource() throws Exception {

		Resource<ID> mockResource2 = this.testDatabase.changeResource(2);

		ResourceService.ReplaceResourceResult<T> mockServiceresult = new ResourceService.ReplaceResourceResult<T>(
				this.testDatabase.getResource(2), false, null);

		Mockito.when(resourceService.replaceResource(ArgumentMatchers.anyString(), ArgumentMatchers.any()))
		.thenReturn(Optional.of(mockServiceresult));


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
	public void replaceResource_RequestNewResource_Return201Created() throws Exception {
		ResourceService.ReplaceResourceResult<T> mockServiceresult = new ResourceService.ReplaceResourceResult<T>(
				this.testDatabase.getResource(2), true, null);

		Mockito.when(resourceService.replaceResource(ArgumentMatchers.anyString(), ArgumentMatchers.any()))
		.thenReturn(Optional.of(mockServiceresult));

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
	public void replaceResource_IdMismatch_ReturnSuccess() throws Exception {

		ResourceService.ReplaceResourceResult<T> mockServiceresult = new ResourceService.ReplaceResourceResult<T>(
				this.testDatabase.getResource(2), false, null);

		Mockito.when(resourceService.replaceResource(ArgumentMatchers.anyString(), ArgumentMatchers.any()))
		.thenReturn(Optional.of(mockServiceresult));

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.put(DefaultResourceController.RELATIVE_PATH + "/" + this.testDatabase.getResource(1).getId())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(asJsonString(this.testDatabase.getResource(2)));

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		Assert.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
	}

	@Test
	public void replaceResource_InvalidPathId_Return400BadRequest() throws Exception {

		ResourceService.ReplaceResourceResult<T> mockServiceresult = new ResourceService.ReplaceResourceResult<T>(
				null, false, "Id type is invalid.");

		Mockito.when(resourceService.replaceResource(Mockito.anyString(), Mockito.any()))
		.thenReturn(Optional.of(mockServiceresult));

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.put(DefaultResourceController.RELATIVE_PATH + "/invalid_typed_id")  // change this if String id is actually ok
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(asJsonString(this.testDatabase.getResource(2)));

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
	}

	@Test
	public void replaceResource_RequestWithEmptyBody_Return400BadRequest() throws Exception {

		// should not call service layer at all
		Mockito.when(resourceService.replaceResource(ArgumentMatchers.anyString(), ArgumentMatchers.any()))
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
	public void replaceResource_RequestWithInvalidBody_Return400BadRequest() throws Exception {

		// should not call service layer at all
		Mockito.when(resourceService.replaceResource(ArgumentMatchers.anyString(), ArgumentMatchers.any()))
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
	public void replaceResource_UnknownServerError_Return500InternalServerError() throws Exception {

		Mockito.when(resourceService.replaceResource(ArgumentMatchers.anyString(), ArgumentMatchers.any()))
		.thenReturn(Optional.empty());

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.put(DefaultResourceController.RELATIVE_PATH + "/" + this.testDatabase.getResource(2).getId())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(asJsonString(this.testDatabase.getResource(2)));

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getResponse().getStatus());
	}

	@Test
	public void insertResource_RequestNewResource_Return201Created() throws Exception {

		Integer new_index = this.testDatabase.saveResource(this.testDatabase.newUnsavedResource());

		Mockito.when(resourceService.insertResource(ArgumentMatchers.any()))
		.thenReturn(Optional.of(this.testDatabase.getResource(new_index)));

		// temporarily assign id=null to get the JSON body for the request
		Resource<ID> new_resource = this.testDatabase.getResource(new_index);
		ID new_id = new_resource.getId();
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
		Resource<ID> resource_result = testDatabase.asResource(result.getResponse().getContentAsString());

		Assert.assertEquals("Id expected to be assigned", resource_result.getId(), new_resource.getId());
	}

	@Test
	public void insertResource_RequestResourceIdAssigned_Return400BadRequest() throws Exception {

		Mockito.when(resourceService.insertResource(ArgumentMatchers.any()))
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

		Mockito.when(resourceService.insertResource(ArgumentMatchers.any()))
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
	public void insertResource_UnknownServerError_Return500InternalServerError() throws Exception {

		Mockito.when(resourceService.insertResource(ArgumentMatchers.any()))
		.thenReturn(Optional.empty());

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post(DefaultResourceController.RELATIVE_PATH)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(asJsonString(this.testDatabase.newUnsavedResource()));

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getResponse().getStatus());
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

		Resource<ID> res = this.testDatabase.newUnsavedResource();
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
