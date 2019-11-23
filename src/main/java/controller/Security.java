package controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class Security {

  @RequestMapping("/login")
  public Mono<String> security() {
    System.out.println("In security controller");
    return Mono.just("This is security.");
  }
}
