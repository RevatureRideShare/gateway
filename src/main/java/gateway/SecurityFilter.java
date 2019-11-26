package gateway;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
    System.out.println("Security filter starting.");
    String requestEndpoint = exchange.getRequest().getURI().getPath();
    HttpMethod requestHttpMethod = exchange.getRequest().getMethod();

    URL obj;
    try {
      System.out.println("HTTP" + "://" + "localhost" + ":" + "8092" + requestEndpoint);
      obj = new URL("HTTP" + "://" + "localhost" + ":" + "8092" + requestEndpoint);

      HttpURLConnection con = (HttpURLConnection) obj.openConnection();
      try {
        List<String> token = exchange.getRequest().getHeaders().get("Authorization");
        System.out.println(token);
        con.setRequestProperty("Authorization", token.get(0));
      } catch (Exception e) {
        e.printStackTrace();
      }
      con.setRequestMethod(requestHttpMethod.toString());
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
        return chain.filter(exchange);
      } else {
        System.out.println("Request did not work. Status Code: " + responseCode);
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.resolve(responseCode));

        return response.setComplete();
      }

    } catch (Exception e) {
      // If the response is bad. Throw error.
      e.printStackTrace();
      return null;
    }
  }

}
