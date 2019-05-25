package trobbie.microrestresource.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Trevor Robbie
 *
 */
@Configuration // denotes class as having @Bean definitions used to configure
@ComponentScan("trobbie.microrestresource")
public class AppConfig {

}