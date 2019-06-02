/**
 *
 */
package trobbie.microrestresource.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
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
import trobbie.microrestresource.service.DefaultResourceService;


/**
 * Test method syntax: MethodName_StateUnderTest_ExpectedBehavior
 *
 * @author Trevor Robbie
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = ResourceController.class)
public class ExampleResourceControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private DefaultResourceService<ExampleResource, Long> resourceService;

	private List<ExampleResource> mockResourceSetEmpty;
	private List<ExampleResource> mockResourceSetA;
	private ExampleResource mockResource1;
	private ExampleResource mockResource2;

	public ExampleResourceControllerTest() {
		this.mockResourceSetEmpty = new ArrayList<ExampleResource>();

		this.mockResource1 = new ExampleResource(1L, "MockResource1");
		this.mockResource2 = new ExampleResource(2L, "MockResource2");

		this.mockResourceSetA = new ArrayList<ExampleResource>();
		this.mockResourceSetA.add(this.mockResource1);
		this.mockResourceSetA.add(this.mockResource2);
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
		Mockito.when(resourceService.saveResource(ArgumentMatchers.any(ExampleResource.class)))
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
	public void upsertResource_RequestIdNotFound_Return400BadRequest() throws Exception {

		Mockito.when(resourceService.saveResource(ArgumentMatchers.any(ExampleResource.class)))
		.thenReturn(Optional.empty());

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.put(DefaultResourceController.RELATIVE_PATH + "/" + mockResource2.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(asJsonString(mockResource2));

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
	}

	@Test
	public void upsertResource_IdMismatch_Return400BadRequest() throws Exception {

		// should not call service layer at all
		Mockito.when(resourceService.saveResource(mockResource2))
		.thenReturn(Optional.empty());

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.put(DefaultResourceController.RELATIVE_PATH + "/" + mockResource1.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(asJsonString(mockResource2));

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());

	}

	private static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}


	}
}
