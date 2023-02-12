package com.fedex.aggregatorservice;

import com.fedex.aggregatorservice.configuration.ServiceConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnableAutoConfiguration
@EnableConfigurationProperties(value = ServiceConfiguration.class)
public
class AggregatorServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
