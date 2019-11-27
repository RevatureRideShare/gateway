package com.revature.gateway;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

  public AuthFilter() {
    super(Config.class);
  }

  @Override
  public GatewayFilter apply(Config config) {
    // Custom Pre-Filter.
    System.out.println("Auth filter starting.");
    return (exchange, chain) -> {
      // For login & registration.
      // Login & registration both need email & password which are contained in a JSON 
      // in the body of the initial request.
      final URI initialUri = exchange.getRequest().getURI();
      final HttpMethod initialHttpMethod = exchange.getRequest().getMethod();
      String initialBody = exchange.getRequest().getBody().toString();
      String email = "NULL";
      String password = "NULL";

      // Extracting email from initial request body.
      Pattern patternEmail = Pattern.compile("\"email\":\"(.*?)\"");
      Matcher matcherEmail = patternEmail.matcher(initialBody);
      if (matcherEmail.find()) {
        System.out.println("Email: " + matcherEmail.group(1));
        email = matcherEmail.group(1);
      }

      // Extracting password from initial request body.
      Pattern patternPassword = Pattern.compile("\"password\":\"(.*?)\"");
      Matcher matcherPassword = patternPassword.matcher(initialBody);
      if (matcherPassword.find()) {
        System.out.println("Password: " + matcherPassword.group(1));
        email = matcherPassword.group(1);
      }

      String finalBody =
          "{\n\t" + "\"email\":\"" + email + "\",\n\t" + "\"password\":\"" + password + "\"\n}";
      System.out.println("Final Body: " + finalBody);

      System.out.println("First pre-filter. URI: " + initialUri + " HttpMethod: "
          + initialHttpMethod + " Body: " + exchange.getRequest().getBody());
      // Custom Post-Filter.
      return chain.filter(exchange).then(Mono.fromRunnable(() -> {
        System.out.println("First post-filter");

        // Determine which service to hit.
        String requestEndpoint = initialUri.getPath();
        String finalPort = "8092";
        String finalHttpMethod = "NULL";

        // Make a new HTTP Request. 
        URL obj;
        try {
          System.out.println("HttpMethod: " + finalHttpMethod);
          System.out.println("HTTP" + "://" + "localhost" + ":" + finalPort + requestEndpoint);
          obj = new URL("HTTP" + "://" + "localhost" + ":" + finalPort + requestEndpoint);

          HttpURLConnection con = (HttpURLConnection) obj.openConnection();
          con.setRequestMethod(finalHttpMethod);
          con.setRequestProperty("Content-Type", "application/json");
          DataOutputStream wr = null;
          try {
            wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(finalBody);
            wr.flush();
            wr.close();
          } catch (IOException exception) {
            throw exception;
          } finally {
            try {
              if (wr != null) {
                wr.close();
              }
            } catch (IOException ex) {
              ex.printStackTrace();
            }
          }
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
