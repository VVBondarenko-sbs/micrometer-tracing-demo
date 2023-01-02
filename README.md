## Migration from Spring Sleuth + Zipkin to Micrometer tracing

### I'm totally lost. Where to start?

As the beginning you can create new app with Spring Initializr, adding Zipkin and Actuator as dependencies. 
It'll add necessary dependencies to Micrometer, Zipkin and Autoconfiguration. 
(Yes, in Spring Boot 3 autoconfig for zipkin is part of actuator) 

It'll work, and it'll give you a working prototype of app, from which you can copy dependencies
in straight-forward manner.

### Will it work out of box?

In general -- yes. With more details -- there'll be difference in default behaviour, and typical configs.
It might break existing tracing chains, especially, if there's manually forwarded spans (e.g. you have intermediate 
python service).

### What is different in configuration?

To have behaviour of Micrometer Tracing, that will be almost identical 
to Spring Sleuth config will change in following manner.

<table>
<thead>
<tr>
    <th>Spring Sleuth + Zipkin Brave</th>
    <th>Micrometer Tracing + Zipkin Brave</th>
</tr>
</thead>
<tbody>
<tr>
<td>

```properties
spring.zipkin.baseUrl=http://localhost:9411/
spring.zipkin.enabled=true
```
</td>
<td>

```properties
management.tracing.enabled=true
management.zipkin.tracing.endpoint=http://localhost:9411/api/v2/spans
management.tracing.sampling.probability=1.0

# might be removed, because uses single-line version of b3, should be overridden with config
management.tracing.propagation.type=b3
```
</td>
</tr>
<tr>
<td></td>
<td>

```java
@Configuration
public class TracingConfig {
  @Bean
  @Primary
  Propagation.Factory propagationFactory(TracingProperties tracing) {
    return B3Propagation.newFactoryBuilder().injectFormat(B3Propagation.Format.MULTI).build();
  }
}
```
</td>
</tr>

</tbody>
</table>

### Known limitations
- Currently, annotations `io.micrometer.tracing.annotation.NewSpan` and `io.micrometer.tracing.annotation.ContinueSpan`
are not working (=> you'll have to write custom aspect to process them)
- Spans won't be started by default for methods annotated with `org.springframework.scheduling.annotation.Scheduled`
and `org.springframework.scheduling.annotation.Async` => you have to define it manually


### Useful links:
[Official migration guide](https://github.com/micrometer-metrics/tracing/wiki/Spring-Cloud-Sleuth-3.1-Migration-Guide)

[Official docs for Micrometer Tracing](https://micrometer.io/docs/tracing)

[B3 propagation definition](https://github.com/openzipkin/b3-propagation)

[Observability in Spring Boot 3 review](https://spring.io/blog/2022/10/12/observability-with-spring-boot-3)

[Beginner's guide to Micrometer Tracing](https://springbootlearning.medium.com/using-micrometer-to-trace-your-spring-boot-app-1fe6ff9982ae)
