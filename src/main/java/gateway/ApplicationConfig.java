package gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableHystrix
@Configuration
public class ApplicationConfig {

  /**
   * ! This bean sets up the routes that Spring Cloud Gateway uses to redirect requests to and from
   * Spring Security along with all Controllers.
   */
  @Bean
  public RouteLocator myRoutes(RouteLocatorBuilder builder) {
    return builder.routes().build();
  }

}
