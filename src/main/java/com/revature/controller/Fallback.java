package com.revature.controller;

import java.util.logging.Logger;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
public class Fallback {
  private static Logger log = Logger.getLogger("FallBackController");

  @RequestMapping("/fallback")
  public Mono<String> fallback() {
    log.info("Inside Fallback's /fallback endpoint");
    return Mono.just("fallback");
  }
}
