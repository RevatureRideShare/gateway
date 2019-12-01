package com.revature.gateway;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

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

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String requestEndpoint = exchange.getRequest().getURI().getPath();
    String host = "localhost";
    String port = "8092";
    HttpMethod requestHttpMethod = exchange.getRequest().getMethod();

    // Checking to see if path hits the POST /login or POST /user endpoints,
    // if the path matches the two endpoints, ignore the rest of the logic.
    if ((requestEndpoint.contentEquals("/login") || requestEndpoint.contentEquals("/user"))
        && requestHttpMethod.equals(HttpMethod.POST)) {
      return chain.filter(exchange).then(Mono.fromRunnable(() -> {
      }));
    }

    try {
      // Creating HTTP Request to the security service.
      URL obj;
      obj = new URL("HTTP://" + host + ":" + port + requestEndpoint);
      HttpURLConnection con = (HttpURLConnection) obj.openConnection();
      con.setRequestMethod(requestHttpMethod.toString());
      try {
        // Attaching JWT to the request header.
        List<String> token = exchange.getRequest().getHeaders().get("Authorization");
        con.setRequestProperty("Authorization", token.get(0));
      } catch (Exception e) {
        // Catching this error is only for testing purposes.
        // Chances are the error will appear because the JWT is not in the original request,
        // which might be fine depending on the request.
        e.printStackTrace();
      }

      // Sending the HTTP Request to the security service.
      int responseCode = con.getResponseCode();
      if (responseCode == HttpURLConnection.HTTP_OK) {
        // If the response code is an "OK".
        // Send the original request to the next filter.
        return chain.filter(exchange);
      } else {
        // If the response is bad, print the response code.
        // Change the response to the response sent by the security 
        // service (probably 403 due to failed authorization).
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.resolve(responseCode));
        // Send the response back to the client.
        return response.setComplete();
      }

    } catch (Exception e) {
      // If creating the HTTP request is bad. Throw error.
      e.printStackTrace();
      return null;
    }

  }

}
