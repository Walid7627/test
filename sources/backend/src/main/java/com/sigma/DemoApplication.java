package com.sigma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


//@SpringBootApplication
@EnableSwagger2
@EnableWebMvc
@SpringBootApplication(exclude = RepositoryRestMvcAutoConfiguration.class)
public class DemoApplication {

  public static void main(String[] args) {
      SpringApplication.run(DemoApplication.class, args);
  }

}
