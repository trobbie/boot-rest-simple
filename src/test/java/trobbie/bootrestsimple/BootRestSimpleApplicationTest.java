package trobbie.bootrestsimple;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import trobbie.bootrestsimple.config.UnitTestConfig;
import trobbie.bootrestsimple.dao.ResourceRepository;
import trobbie.bootrestsimple.dao.TestDatabase;
import trobbie.bootrestsimple.model.ExampleResource;
import trobbie.bootrestsimple.service.DefaultResourceService;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = { UnitTestConfig.class } )
public class BootRestSimpleApplicationTest {

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

	@Autowired
	private TestDatabase<ExampleResource, Long> testDatabase;

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
		Assert.assertNotNull(testDatabase);
	}

}
