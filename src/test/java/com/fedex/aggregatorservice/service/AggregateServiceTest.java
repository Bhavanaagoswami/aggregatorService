package com.fedex.aggregatorservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fedex.aggregatorservice.AggregatorServiceApplicationTests;
import com.fedex.aggregatorservice.configuration.ServiceConfiguration;
import com.fedex.aggregatorservice.model.Aggregate;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AggregatorServiceApplicationTests.class)
public class AggregateServiceTest {

    @Autowired
    ServiceConfiguration serviceConfiguration;
    @InjectMocks
    private AggregateService aggregateService;
    @MockBean
    private TrackService trackService;
    @MockBean
    private ShipmentService shipmentService;
    @MockBean
    private PricingService pricingService;

    @Before
    public void setUp() {
        this.shipmentService = mock(ShipmentService.class);
        this.trackService = mock(TrackService.class);
        this.pricingService = mock(PricingService.class);
        shipmentService = Mockito.spy(new ShipmentService(serviceConfiguration));
        trackService = Mockito.spy(new TrackService(serviceConfiguration));
        pricingService = Mockito.spy(new PricingService(serviceConfiguration));
        aggregateService = Mockito.spy(new AggregateService(shipmentService, trackService, pricingService));
    }

    @Test
    public void getConfig() {
        assertNotNull(serviceConfiguration.getTimeout());
        assertNotNull(serviceConfiguration.getBaseUrl());
    }

    @Test
    public void getShipmentResponse() throws IOException {

        List<Long> orderNumber = List.of(111111111L);
        String shipmentResponseString = "[\"BOX\",\"ENVELOPE\"]";
        List<String> shipmentServiceResponse = new ArrayList<>();
        shipmentServiceResponse.add("[BOX,ENVELOPE]");

        Aggregate aggregate = new Aggregate();
        Map<String, List<String>> map = new HashMap<>();
        map.put(orderNumber.get(0).toString(), shipmentServiceResponse);
        aggregate.setShipments(map);

        when(shipmentService.callExternalShipmentService(anyLong())).thenReturn(CompletableFuture.completedFuture(shipmentResponseString));
        Aggregate shipmentResponse = aggregateService.getAllApiAggregateDetails(orderNumber, null, null);
        log.info("Response Object : {}", shipmentResponse);
        assertNotNull(shipmentResponse);
        assertEquals(shipmentResponse.getShipments(), aggregate.getShipments());
    }

    @Test
    public void getTrackResponse() throws JsonProcessingException {
        List<Long> orderNumber = List.of(111111111L);
        String trackResponseString = "[\"COLLECTED\"]";
        List<String> trackServiceResponse = new ArrayList<>();
        trackServiceResponse.add("[COLLECTED]");

        Aggregate aggregate = new Aggregate();
        Map<String, List<String>> map = new HashMap<>();
        map.put(orderNumber.get(0).toString(), trackServiceResponse);
        aggregate.setTrack(map);

        when(trackService.getTrackStatusDetails(anyLong())).thenReturn(CompletableFuture.completedFuture(trackResponseString));
        Aggregate trackResponse = aggregateService.getAllApiAggregateDetails(null, orderNumber, null);
        log.info("Response Object : {}", trackResponse);
        assertNotNull(trackResponse);
        assertEquals(trackResponse.getTrack(), aggregate.getTrack());
    }

    @Test
    public void getPricingResponse() throws ExecutionException, InterruptedException, JsonProcessingException {
        List<String> countryCodes = List.of("NL");
        Double pricingResponse = 14.5656565;

        Aggregate aggregate = new Aggregate();
        Map<String, Double> map = new HashMap<>();
        map.put(countryCodes.get(0).toString(), pricingResponse);
        aggregate.setPricing(map);

        when(pricingService.getPricingDetails(anyString())).thenReturn(CompletableFuture.completedFuture(pricingResponse.toString()));
        Aggregate pricingAggregate = aggregateService.getAllApiAggregateDetails(null, null, countryCodes);
        log.info("Response Object : {}", pricingAggregate);
        assertNotNull(pricingAggregate);
        assertEquals(aggregate.getPricing(), pricingAggregate.getPricing());
    }

}
