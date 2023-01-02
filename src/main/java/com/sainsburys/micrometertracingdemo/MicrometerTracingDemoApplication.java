package com.sainsburys.micrometertracingdemo;

import brave.propagation.B3Propagation;
import brave.propagation.Propagation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.tracing.TracingProperties;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableScheduling
@EnableAspectJAutoProxy
public class MicrometerTracingDemoApplication {

  public static void main(String[] args) {
    SpringApplication.run(MicrometerTracingDemoApplication.class, args);
  }

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {
    return builder.build();
  }

  @Bean
  @Primary
  Propagation.Factory propagationFactory(TracingProperties tracing) {
    return B3Propagation.newFactoryBuilder().injectFormat(B3Propagation.Format.MULTI).build();
  }
}
