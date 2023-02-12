package com.fedex.aggregatorservice.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "external.service")
@Getter
@Setter
@NoArgsConstructor
@PropertySource(
        value = {"classpath:application.properties"}
)
public class ServiceConfiguration {
 private String baseUrl;
 private Long timeout;

 public ServiceConfiguration(String baseUrl, Long timeout) {
  this.baseUrl = baseUrl;
  this.timeout = timeout;
 }
}
