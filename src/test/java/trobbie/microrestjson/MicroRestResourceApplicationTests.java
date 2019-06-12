package trobbie.microrestjson;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import trobbie.microrestjson.dao.ResourceRepository;
import trobbie.microrestjson.model.ExampleResource;
import trobbie.microrestjson.service.DefaultResourceService;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class MicroRestResourceApplicationTests {

	@Bean
	public DefaultResourceService<ExampleResource, Long> resourceService() {
		return new DefaultResourceService<ExampleResource, Long>() {
			@Override
			public Long stringToIDConverter(String idString) {
				return Long.parseLong(idString);
			}
		};
	}

	@Autowired
	private ApplicationContext appContext;

	@Autowired
	private ResourceRepository<ExampleResource, Long> resourceRepository;

	@Autowired
	private DefaultResourceService<ExampleResource, Long> resourceService;

	@Test
	public void contextLoads() {
		// this empty function just tests to see if startup throws an exception
		// e.g. dataSource UnsatisfiedDependencyException if a repository implementation not found on the classpath
	}

	@Test
	public void checkConfig()
	{
		Assert.assertNotNull(appContext);
		Assert.assertNotNull(resourceRepository);
		Assert.assertNotNull(resourceService);
	}
}
