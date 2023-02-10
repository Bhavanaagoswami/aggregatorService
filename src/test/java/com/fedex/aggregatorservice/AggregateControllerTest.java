package com.fedex.aggregatorservice;

import com.fedex.aggregatorservice.controller.AggregateController;
import com.fedex.aggregatorservice.model.Aggregate;
import com.fedex.aggregatorservice.service.AggregateService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AggregateControllerTest {

    @InjectMocks
    private AggregateController aggregateController;

    private AggregateService aggregateService;

    @Before
    public void setup() {
        this.aggregateService = mock(AggregateService.class);
        this.aggregateController = new AggregateController(aggregateService);
    }

    @Test
    public void getAllApiResponse() throws Throwable {
        //GIVEN
        List<Long> shipmentOrderNumber = List.of(111111111L,22222222L);
        List<Long> trackNumber = null;
        List<String> pricing = List.of("UK","IN","FR");
        String shipmentResponseString = "[\"BOX\",\"ENVELOPE\"]";
        String pricingResponseString = "[\"DELIVERING\"]";
        List<Object> shipmentServiceResponse = Arrays.asList("BOX","ENVELOPE");
        List<Object> priceServiceResponse = Arrays.asList("DELIVERING");

        //Expected Response Object
        Aggregate aggregate = new Aggregate();
        HashMap<String,List<Object>> map = new HashMap<>();
        map.put("111111111",shipmentServiceResponse);
        aggregate.setShipments(map);

        map = new HashMap<>();
        map.put("111111111",priceServiceResponse);
        aggregate.setPricing(map);

        when(aggregateService.getShipmentOrderDetails(anyLong()))
                .thenReturn(CompletableFuture.completedFuture(shipmentResponseString));
        when(aggregateService.getTrackStatusDetails(anyLong()))
                .thenReturn(CompletableFuture.completedFuture(null));
        when(aggregateService.getPricingDetails(anyString()))
                .thenReturn(CompletableFuture.completedFuture(pricingResponseString));

        //WHEN
        ResponseEntity<Aggregate> result = aggregateController.getAllDetails(shipmentOrderNumber,null,pricing);

        //THEN
        assertEquals("Compare responses ",aggregate, result.getBody());
    }

}
