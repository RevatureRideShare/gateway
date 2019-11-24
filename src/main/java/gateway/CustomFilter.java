package gateway;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

@Component
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> {
  public CustomFilter() {
    super(Config.class);
  }

  @Override
  public GatewayFilter apply(Config config) {
    //Custom Pre-Filter.
    return (exchange, chain) -> {
      System.out.println("First pre filter" + exchange.getRequest());
      //Custom Post-Filter.
      return chain.filter(exchange).then(Mono.fromRunnable(() -> {
        System.out.println("First post filter");

        // Make a new HTTP Request
        URL obj;
        try {
          obj = new URL("http://www.google.com");

          HttpURLConnection con = (HttpURLConnection) obj.openConnection();
          con.setRequestMethod("GET");
          int responseCode = con.getResponseCode();
          if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
              response.append(inputLine);
            }
            in.close();

            // print result
            System.out.println(response.toString());
          } else {
            System.out.println("Request did not work. Status Code: " + responseCode);
          }

        } catch (Exception e) {
          // If the response is bad. Throw error.
          e.printStackTrace();
        }
      }));
    };
  }

  public static class Config {
    // Put the configuration properties
  }
}
