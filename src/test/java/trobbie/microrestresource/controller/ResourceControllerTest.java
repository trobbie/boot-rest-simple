/**
 *
 */
package trobbie.microrestresource.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
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

import trobbie.microrestresource.model.Resource;
import trobbie.microrestresource.service.ResourceService;


/**
 * Test method syntax: MethodName_StateUnderTest_ExpectedBehavior
 *
 * @author Trevor Robbie
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = ResourceController.class)
public class ResourceControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ResourceService resourceService;

	private List<Resource> mockResourceSetEmpty;
	private List<Resource> mockResourceSetA;
	private Resource mockResource1;

	public ResourceControllerTest() {
		this.mockResourceSetEmpty = new ArrayList<Resource>();

		this.mockResource1 = new Resource(1, "MockResource1");

		this.mockResourceSetA = new ArrayList<Resource>();
		this.mockResourceSetA.add(this.mockResource1);
	}

	@Test
	public void getResources_RequestedOnEmptyData_ReturnEmptyList() throws Exception {

		Mockito.when(resourceService.getResources())
		.thenReturn(mockResourceSetEmpty);

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get(ResourceController.RELATIVE_PATH)
				.accept(MediaType.APPLICATION_JSON);

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
				.get(ResourceController.RELATIVE_PATH)
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String expected = "[{id:1,name:MockResource1}]";

		Assert.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}

	@Test
	public void getResource_IdFound_ReturnResource() throws Exception {
		Mockito.when(resourceService.getResource(mockResource1.getId()))
		.thenReturn(mockResource1);

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get(ResourceController.RELATIVE_PATH + "/" + mockResource1.getId())
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String expected = "{id:1,name:MockResource1}";

		Assert.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);

	}

	@Test
	public void getResource_IdUnknown_Return404NotFound() throws Exception {
		Long unknownId = 99999L;

		Mockito.when(resourceService.getResource(unknownId))
		.thenReturn(null);

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get(ResourceController.RELATIVE_PATH + "/" + unknownId)
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		Assert.assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());

	}

	@Test
	public void getResource_StringIdRequested_Return400BadRequest() throws Exception {

		// should not call service layer at all
		Mockito.when(resourceService.getResource(Mockito.anyLong()))
		.thenThrow(RuntimeException.class);

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get(ResourceController.RELATIVE_PATH + "/stringtest")
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());

	}


}
