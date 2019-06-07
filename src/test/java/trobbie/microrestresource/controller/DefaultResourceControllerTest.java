/**
 *
 */
package trobbie.microrestresource.controller;

import java.util.ArrayList;
import java.util.List;
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

import trobbie.microrestresource.model.ExampleResource;
import trobbie.microrestresource.model.Resource;
import trobbie.microrestresource.service.DefaultResourceService;


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

	private List<ExampleResource> mockResourceSetEmpty;
	private List<ExampleResource> mockResourceSetA;
	private ExampleResource mockResource1;
	private ExampleResource mockResource2;
	private ExampleResource mockResource3;
	private ExampleResource mockResource3noid;

	public DefaultResourceControllerTest() {
		createTestDataStructures();
	}

	private void createTestDataStructures() {
		this.mockResourceSetEmpty = new ArrayList<ExampleResource>();
		this.mockResourceSetA = new ArrayList<ExampleResource>();
	}

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

		// ensure empty
		this.mockResourceSetEmpty.clear();

		// recreate, in case ever changed in tests
		this.mockResource1 = new ExampleResource(1L, "MockResource1");
		this.mockResource2 = new ExampleResource(2L, "MockResource2");


		this.mockResourceSetA.clear();
		this.mockResourceSetA.add(this.mockResource1);
		this.mockResourceSetA.add(this.mockResource2);

		this.mockResource3 = new ExampleResource(3L, "MockResource3");
		this.mockResource3noid = new ExampleResource(null, "MockResource3");
	}


	@Test
	public void getResources_RequestedOnEmptyData_ReturnEmptyList() throws Exception {

		Mockito.when(resourceService.getResources())
		.thenReturn(mockResourceSetEmpty);

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
	public void getResources_Requested_ReturnResourceList() throws Exception {

		Mockito.when(resourceService.getResources())
		.thenReturn(mockResourceSetA);

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
		.thenReturn(Optional.of(mockResource1));

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get(DefaultResourceController.RELATIVE_PATH + "/" + mockResource1.getId())
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8");

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		String expected = asJsonString(mockResource1);
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
		.thenThrow(RuntimeException.class);

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get(DefaultResourceController.RELATIVE_PATH + "/stringtest")
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8");

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
	}

	@Test
	public void upsertResource_Requested_ReturnSameResource() throws Exception {

		mockResource2.setName("MockResource2update");
		Mockito.when(resourceService.getResource(mockResource2.getId().toString()))
		.thenReturn(Optional.of(mockResource2));
		Mockito.when(resourceService.saveResource(ArgumentMatchers.any(RESOURCE_CLASS)))
		.thenReturn(Optional.of(mockResource2));

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.put(DefaultResourceController.RELATIVE_PATH + "/" + mockResource2.getId())
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
				.put(DefaultResourceController.RELATIVE_PATH + "/" + mockResource1.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(asJsonString(mockResource2));

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
				.put(DefaultResourceController.RELATIVE_PATH + "/" + mockResource2.getId())
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
				.put(DefaultResourceController.RELATIVE_PATH + "/" + mockResource2.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content("[_id='invalidentity'");

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
	}

	@Test
	public void upsertResource_RequestNewResource_Return201Created() throws Exception {

		Mockito.when(resourceService.getResource(mockResource2.getId().toString()))
		.thenReturn(Optional.empty());
		Mockito.when(resourceService.saveResource(ArgumentMatchers.any(RESOURCE_CLASS)))
		.thenReturn(Optional.of(mockResource2));

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.put(DefaultResourceController.RELATIVE_PATH + "/" + mockResource2.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(asJsonString(mockResource2));

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		Assert.assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());
	}

	@Test
	public void insertResource_RequestNewResource_Return201Created() throws Exception {

		Mockito.when(resourceService.createResource(ArgumentMatchers.any(RESOURCE_CLASS)))
		.thenReturn(Optional.of(mockResource3));

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post(DefaultResourceController.RELATIVE_PATH)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(asJsonString(mockResource3noid));

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		Assert.assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());
		Resource r = asResource(result.getResponse().getContentAsString());

		Assert.assertNotNull("Id expected to be created as non-null", r.getId());
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
				.content(asJsonString(mockResource3));

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

}
