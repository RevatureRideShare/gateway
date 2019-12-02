package com.revature.gateway;

import java.util.logging.Logger;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableHystrix
@Configuration
public class ApplicationConfig {
  private static Logger log = Logger.getLogger("ApplicationConfig");

  /**
   * This bean sets up the routes that Spring Cloud Gateway uses to redirect requests to and from
   * Spring Security along with all Controllers.
   */
  @Bean
  public RouteLocator myRoutes(RouteLocatorBuilder builder) {
    log.info("Inside ApplicationConfig's myRoutes method with RouteLocatorBuilder "
        + builder.toString());
    return builder.routes().build();
  }

}
