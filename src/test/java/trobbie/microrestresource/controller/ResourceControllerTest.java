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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import trobbie.microrestresource.model.Resource;
import trobbie.microrestresource.service.ResourceService;


/**
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

	private Resource mockResource1;
	private List<Resource> mockResources;

	public ResourceControllerTest() {
		this.mockResource1 = new Resource(1, "MockResource1");
		this.mockResources = new ArrayList<Resource>();
		this.mockResources.add(this.mockResource1);
	}

	@Test
	public void getResources_Requested_ReturnResourceList() throws Exception {

		Mockito.when(resourceService.getResources())
		.thenReturn(mockResources);

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/resources")
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String expected = "[{id:1,name:MockResource1}]";

		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}

	@Test
	public void getResource_IdFound_ReturnResource() throws Exception {
		Mockito.when(resourceService.getResource(mockResource1.getId()))
		.thenReturn(mockResource1);

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/resources/" + mockResource1.getId())
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String expected = "{id:1,name:MockResource1}";

		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);


	}

	@Test
	public void getResource_IdUnknown_Return404() throws Exception {
		Long unknownId = 99999L;

		Mockito.when(resourceService.getResource(unknownId))
		.thenReturn(null);

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/resources/" + unknownId)
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		Assert.assertEquals(404, result.getResponse().getStatus());

	}


}
