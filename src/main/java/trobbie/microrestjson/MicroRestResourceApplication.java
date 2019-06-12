package trobbie.microrestjson;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import trobbie.microrestjson.model.ExampleResource;
import trobbie.microrestjson.service.DefaultResourceService;

@SpringBootApplication
public class MicroRestResourceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroRestResourceApplication.class, args);
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
