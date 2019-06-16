/*
 * Copyright (c) 2019 Trevor Robbie
 * This program is made available under the terms of the MIT License.
 */
/**
 *
 */
package trobbie.bootrestsimple.controller;

import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import trobbie.bootrestsimple.config.UnitTestConfig;
import trobbie.bootrestsimple.model.ExampleResource;

/**
 * @author Trevor Robbie
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = ResourceController.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = { UnitTestConfig.class } )
public class ExampleResourceControllerTest extends DefaultResourceControllerTest<ExampleResource, Long> {

}
