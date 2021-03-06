package trobbie.bootrestsimple.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Trevor Robbie
 *
 */
@Configuration // denotes class as having @Bean definitions used to configure
@ComponentScan("trobbie.bootrestsimple")
public class AppConfig {

}