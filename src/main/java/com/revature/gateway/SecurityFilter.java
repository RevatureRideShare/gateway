package com.revature.gateway;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class SecurityFilter implements GlobalFilter {

  private static Logger log = Logger.getLogger("SecurityFilter");

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    log.info("Inside SecurityFilter's filter method with ServerWebExchange " + exchange.toString()
        + " and GatewayFilterChain " + chain);
    String requestEndpoint = exchange.getRequest().getURI().getPath();
    log.info("RequestEndpoint is " + requestEndpoint);
    String host = "localhost";
    String port = "8092";
    HttpMethod requestHttpMethod = exchange.getRequest().getMethod();

    log.info("Request Http Method is " + requestHttpMethod.toString());

    // Checking to see if path hits the POST /login or POST /user endpoints,
    // if the path matches the two endpoints, ignore the rest of the logic.
    if ((requestEndpoint.contentEquals("/login") || requestEndpoint.contentEquals("/user"))
        && requestHttpMethod.equals(HttpMethod.POST)) {
      log.info("Request Endpoint is either /login, OR the endpoint is a POST /user");
      return chain.filter(exchange).then(Mono.fromRunnable(() -> {
      }));
    }

    if ((requestEndpoint.contentEquals("/training-location")
        || requestEndpoint.contentEquals("/housing-location"))
        && requestHttpMethod.equals(HttpMethod.GET)) {
      log.info("Request Endpoint is either GET /training-location, OR the endpoint is "
          + "a /housing-location");
      return chain.filter(exchange).then(Mono.fromRunnable(() -> {
      }));
    }

    try {
      // Creating HTTP Request to the security service.
      URL obj;
      obj = new URL("HTTP://" + host + ":" + port + requestEndpoint);
      log.info("Trying URL of " + obj);
      HttpURLConnection con = (HttpURLConnection) obj.openConnection();
      con.setRequestMethod(requestHttpMethod.toString());
      try {
        // Attaching JWT to the request header.
        if (requestHttpMethod.equals(HttpMethod.OPTIONS)) {
          log.info("Request is an OPTIONS request.");
        } else {
          List<String> token = exchange.getRequest().getHeaders().get("Authorization");
          log.info("List of tokens is " + token.toString());
          con.setRequestProperty("Authorization", token.get(0));
        }
      } catch (Exception e) {
        // Catching this error is only for testing purposes.
        // Chances are the error will appear because the JWT is not in the original request,
        // which might be fine depending on the request.
        log.info("Generic exception, probably because JWT is not in the original request");
        e.printStackTrace();
      }

      // Sending the HTTP Request to the security service.
      int responseCode = con.getResponseCode();
      if (responseCode == HttpURLConnection.HTTP_OK) {
        // If the response code is an "OK".
        // Send the original request to the next filter.
        log.info("Response code is HTTP_OK");
        return chain.filter(exchange);
      } else {
        // If the response is bad, print the response code.
        // Change the response to the response sent by the security
        // service (probably 403 due to failed authorization).
        log.info("Response code is " + responseCode);
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.resolve(responseCode));
        // Send the response back to the client.
        log.info("Sending the response back to the client");
        return response.setComplete();
      }

    } catch (

    Exception e) {
      // If creating the HTTP request is bad. Throw error.
      log.info("Something went wrong with creating the HTTP request");
      e.printStackTrace();
      return null;
    }

  }

}
