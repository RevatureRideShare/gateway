package com.revature.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class Fallback {
  @RequestMapping("/fallback")
  public Mono<String> fallback() {
    return Mono.just("fallback");
  }
}
