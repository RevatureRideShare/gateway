package com.revature.gateway;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.regex.Pattern;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

@Component
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> {
  private final String gatewayPort = "8088";
  private final String locationPort = "8089";
  private final String userPort = "8090";
  private final String adminPort = "8091";
  // Security port is never used but kept for reference, because login requests need 
  // to be directed to  the user microservice in addition to hitting the security 
  // service so that the user service can return a User object to the front end.
  private final String securityPort = "8092";

  private final String[] securityHttpMethods = {"POST"};
  private final String[] securityEndpoints = {"/login"};

  // DELETE user might should not point to admin?
  private final String[] adminHttpMethods = {"DELETE", "POST", "GET"};
  private final String[] adminEndpoints = {"/user/.*", "/admin", "/admin"};

  private final String[] userHttpMethods = {"POST", "PUT", "PATCH", "GET", "GET", "GET"};
  private final String[] userEndpoints =
      {"/user", "/user/.*", "/user/.*", "/user?role=.*", "/user", "/user/.*"};

  private final String[] carHttpMethods = {"POST", "GET"};
  private final String[] carEndpoints = {"/car", "/user/.*/car"};

  private final String[] housingLocationHttpMethods = {"POST", "GET", "GET"};
  private final String[] housingLocationEndpoints =
      {"/housing-location", "/housing-location", "/housing-location/.*/housing-location"};

  private final String[] trainingLocationHttpMethods = {"POST", "GET"};
  private final String[] trainingLocationEndpoints = {"/training-location", "/training-location"};

  private URI initialUri;
  private HttpMethod initialHttpMethod;

  public CustomFilter() {
    super(Config.class);
  }

  @Override
  public GatewayFilter apply(Config config) {
    // Custom Pre-Filter.
    return (exchange, chain) -> {
      initialUri = exchange.getRequest().getURI();
      initialHttpMethod = exchange.getRequest().getMethod();
      System.out.println("First pre-filter. URI: " + initialUri + " HttpMethod: "
          + initialHttpMethod + " Body: " + exchange.getRequest().getBody());
      // Custom Post-Filter.
      return chain.filter(exchange).then(Mono.fromRunnable(() -> {
        System.out.println("First post-filter");

        // Determine which service to hit.
        String requestEndpoint = initialUri.getPath();
        String finalPort = "0";
        String finalHttpMethod = "NULL";

        for (int i = 0; i < securityEndpoints.length; i++) {
          boolean endpointMatch = Pattern.matches(securityEndpoints[i], requestEndpoint);
          boolean httpMethodMatch =
              Pattern.matches(securityHttpMethods[i], initialHttpMethod.toString());
          if (endpointMatch == true && httpMethodMatch == true) {
            // Logging in requires returning a user and does not follow the 
            // typical endpoint flow, so it is handled specially.
            finalPort = userPort;
            finalHttpMethod = userHttpMethods[4];
            requestEndpoint = userEndpoints[4];
          }
        }

        for (int i = 0; i < adminEndpoints.length; i++) {
          boolean endpointMatch = Pattern.matches(adminEndpoints[i], requestEndpoint);
          boolean httpMethodMatch =
              Pattern.matches(adminHttpMethods[i], initialHttpMethod.toString());
          if (endpointMatch == true && httpMethodMatch == true) {
            finalPort = adminPort;
            finalHttpMethod = adminHttpMethods[i];
          }
        }

        for (int i = 0; i < userEndpoints.length; i++) {
          boolean endpointMatch = Pattern.matches(userEndpoints[i], requestEndpoint);
          boolean httpMethodMatch =
              Pattern.matches(userHttpMethods[i], initialHttpMethod.toString());
          if (endpointMatch == true && httpMethodMatch == true) {
            finalPort = userPort;
            finalHttpMethod = userHttpMethods[i];
          }
        }

        for (int i = 0; i < carEndpoints.length; i++) {
          boolean endpointMatch = Pattern.matches(carEndpoints[i], requestEndpoint);
          boolean httpMethodMatch =
              Pattern.matches(carHttpMethods[i], initialHttpMethod.toString());
          if (endpointMatch == true && httpMethodMatch == true) {
            // Car functionality lives within the user service, so the actions are 
            // directed to the user service.
            finalPort = userPort;
            finalHttpMethod = carHttpMethods[i];
          }
        }

        for (int i = 0; i < housingLocationEndpoints.length; i++) {
          boolean endpointMatch = Pattern.matches(housingLocationEndpoints[i], requestEndpoint);
          boolean httpMethodMatch =
              Pattern.matches(housingLocationHttpMethods[i], initialHttpMethod.toString());
          if (endpointMatch == true && httpMethodMatch == true) {
            // Housing location lives in location service.
            finalPort = locationPort;
            finalHttpMethod = housingLocationHttpMethods[i];
          }
        }

        for (int i = 0; i < trainingLocationEndpoints.length; i++) {
          boolean endpointMatch = Pattern.matches(trainingLocationEndpoints[i], requestEndpoint);
          boolean httpMethodMatch =
              Pattern.matches(trainingLocationHttpMethods[i], initialHttpMethod.toString());
          if (endpointMatch == true && httpMethodMatch == true) {
            finalPort = locationPort;
            finalHttpMethod = trainingLocationHttpMethods[i];
          }
        }

        // Make a new HTTP Request. 
        URL obj;
        try {
          System.out.println("HttpMethod: " + finalHttpMethod);
          System.out.println("URL: " + initialUri.getScheme() + "://" + initialUri.getHost() + ":"
              + finalPort + requestEndpoint);
          obj = new URL(initialUri.getScheme() + "://" + initialUri.getHost() + ":" + finalPort
              + requestEndpoint);

          HttpURLConnection con = (HttpURLConnection) obj.openConnection();
          con.setRequestMethod(finalHttpMethod);
          int responseCode = con.getResponseCode();
          // If the response code is an "OK".
          if (responseCode == HttpURLConnection.HTTP_OK) {
            // Get the response.
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
              response.append(inputLine);
            }
            in.close();

            // Print the response. 
            System.out.println(response.toString());
          } else {
            System.out.println("Request did not work. Status Code: " + responseCode);
          }

        } catch (Exception e) {
          // If the response is bad. Throw error.
          e.printStackTrace();
        }
      }

      ));
    };
  }

  public static class Config {
    // Put the configuration properties
  }
}
