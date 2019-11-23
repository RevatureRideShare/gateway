package gateway;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
public class UriConfiguration {

  private String gatewayHost = "**localhost:8088";
  private String securityHost = "http://localhost:8092";
  private String hystrixHost = "http://httpbin.org:80";

  public String getSecurityHost() {
    return securityHost;
  }

  public void setSecurityHost(String securityHost) {
    this.securityHost = securityHost;
  }

  public String getGatewayHost() {
    return gatewayHost;
  }

  public void setGatewayHost(String gatewayHost) {
    this.gatewayHost = gatewayHost;
  }

  public String getHystrixHost() {
    return hystrixHost;
  }

  public void setHystrixHost(String hystrixHost) {
    this.hystrixHost = hystrixHost;
  }

}
