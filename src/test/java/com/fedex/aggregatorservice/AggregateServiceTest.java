package com.fedex.aggregatorservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fedex.aggregatorservice.configuration.ServiceConfiguration;
import com.fedex.aggregatorservice.model.Aggregate;
import com.fedex.aggregatorservice.service.AggregateService;
import com.fedex.aggregatorservice.service.PricingService;
import com.fedex.aggregatorservice.service.ShipmentService;
import com.fedex.aggregatorservice.service.TrackService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@ContextConfiguration(classes = {ServiceConfiguration.class})
public class AggregateServiceTest {

    @InjectMocks
    private AggregateService aggregateService;

    @Mock
    private TrackService trackService;
    @Mock
    private ShipmentService shipmentService;
    @Mock
    private PricingService pricingService;

    @Autowired
    ServiceConfiguration serviceConfiguration;


    @Before
    public void setUp() {
        this.shipmentService = mock(ShipmentService.class);
        this.trackService = mock(TrackService.class);
        this.pricingService = mock(PricingService.class);
        shipmentService = Mockito.spy(new ShipmentService(this.serviceConfiguration));
        aggregateService = Mockito.spy(new AggregateService(shipmentService, trackService, pricingService));
    }

    @Test
    public void getConfig(){
        assertNotNull(this.serviceConfiguration.getBaseUrl());
    }

    @Test
    public void getShipmentResponse() throws IOException, InterruptedException {

        List<Long> orderNumber = List.of(111111111L);
        String shipmentResponseString = "[\"BOX\",\"ENVELOPE\"]";
        List<Object> shipmentServiceResponse = Arrays.asList("BOX", "ENVELOPE");

        Aggregate aggregate = new Aggregate();
        Map<String, List<Object>> map = new HashMap<>();
        map.put(orderNumber.get(0).toString(), shipmentServiceResponse);
        aggregate.setShipments(map);
        Map mockMap = mock(Map.class);

        String pricingResponseString = "[\"DELIVERING\"]";
        when(shipmentService.callExternalShipmentService(anyLong())).thenReturn(CompletableFuture.completedFuture(shipmentResponseString));
        Aggregate shipmentResponse = aggregateService.getAllApiAggregateDetails(orderNumber,null,null);
        log.info("Response Object : {}",shipmentResponse);
        assertNotNull(shipmentResponse);
        assertEquals(shipmentResponse,aggregate);
    }

    @Test
    public void getTrackResponse() throws ExecutionException, InterruptedException, JsonProcessingException {
        CompletableFuture<String> trackResponse = trackService.getTrackStatusDetails(100454545L);

        log.info("Response Object : {}",trackResponse.get());
        assertNotNull(trackResponse.get());
    }

    @Test
    public void getPricingResponse() throws ExecutionException, InterruptedException, JsonProcessingException {
        CompletableFuture<String> pricingResponse = pricingService.getPricingDetails("NL");

        log.info("Response Object : {}",pricingResponse.get());
        assertNotNull(pricingResponse.get());
    }

}
