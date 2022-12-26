package com.sainsburys.micrometertracingdemo;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
public class Controller {
  private final ApplicationContext context;
  private final RestTemplate restTemplate;

  @GetMapping("/")
  public String ok(){
    return "ok";
  }

  @GetMapping("/call")
  public String call() {
//    return restTemplate.getForObject("https://httpbin.org/headers", JsonNode.class);
    return restTemplate.getForObject("http://127.0.0.1:5000/", String.class);
  }
}
