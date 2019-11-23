package gateway;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties(UriConfiguration.class)
@EnableHystrix
@Configuration
public class ApplicationConfig {



  /**
   * ! This bean sets up the routes that Spring Cloud Gateway uses to redirect requests to and from
   * Spring Security along with all Controllers.
   */
  @Bean
  public RouteLocator myRoutes(RouteLocatorBuilder builder, UriConfiguration uriConfiguration) {
    String gatewayHost = uriConfiguration.getGatewayHost();
    String securityHost = uriConfiguration.getSecurityHost();
    String hystrixHost = uriConfiguration.getHystrixHost();
    return builder.routes()
        .route(p -> p.host(gatewayHost).and().path("/actuator/hystrix.stream")
            .filters(f -> f.hystrix(config -> config.setName("mycmd"))).uri(hystrixHost))

        .route(r -> r.host(gatewayHost).and().path("/**")
            .filters(f -> f.hystrix(config -> config.setName("security-service")))
            .uri(securityHost))

        .route(p -> p.host("*.hystrix.com")
            .filters(f -> f.hystrix(config -> config.setName("mycmd"))).uri(hystrixHost))

        .build();
  }

}
