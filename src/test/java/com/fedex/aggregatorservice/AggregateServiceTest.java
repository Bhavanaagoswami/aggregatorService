package com.fedex.aggregatorservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fedex.aggregatorservice.model.Aggregate;
import com.fedex.aggregatorservice.service.AggregateService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertNotNull;

@Slf4j
@ActiveProfiles("test")
@TestPropertySource(locations="classpath:application-test.properties")
public class AggregateServiceTest {

    @Value("{service.url}")
    private String serviceUrl;

    @Value("{service.timeout}")
    private Long serviceTimeout;

    private AggregateService aggregateService;

    @Before
    public void setUp() {
        aggregateService = Mockito.spy(new AggregateService());
    }

    @Test
    public void getShipmentResponse() throws ExecutionException, InterruptedException, JsonProcessingException {
        Aggregate shipmentResponse = aggregateService.getAllApiDetails(List.of(100454545L),null,null);
        log.info("Response Object : {}",shipmentResponse);
        assertNotNull(shipmentResponse);
    }

    @Test
    public void getTrackResponse() throws ExecutionException, InterruptedException, JsonProcessingException {
        CompletableFuture<String> trackResponse = aggregateService.getTrackStatusDetails(100454545L);

        log.info("Response Object : {}",trackResponse.get());
        assertNotNull(trackResponse.get());
    }

    @Test
    public void getPricingResponse() throws ExecutionException, InterruptedException, JsonProcessingException {
        CompletableFuture<String> pricingResponse = aggregateService.getPricingDetails("NL");

        log.info("Response Object : {}",pricingResponse.get());
        assertNotNull(pricingResponse.get());
    }

}
