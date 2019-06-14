package trobbie.bootrestsimple;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import trobbie.bootrestsimple.model.ExampleResource;
import trobbie.bootrestsimple.service.DefaultResourceService;

@SpringBootApplication
public class BootRestSimpleApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootRestSimpleApplication.class, args);
	}

	@Bean
	public DefaultResourceService<ExampleResource, Long> resourceService() {
		return new DefaultResourceService<ExampleResource, Long>() {
			@Override
			public Long stringToIDConverter(String idString) {
				try {
					return Long.parseLong(idString);
				} catch (NumberFormatException e) {
					return null;
				}

			}
		};
	}

}
