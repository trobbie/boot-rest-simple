package trobbie.microrestresource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class MicroRestResourceApplicationTests {

	@Test
	public void contextLoads() {
		// this empty function just tests to see if startup throws an exception
		// e.g. dataSource UnsatisfiedDependencyException if a repository implementation not found on the classpath
	}

}
