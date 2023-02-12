package com.fedex.aggregatorservice;

import com.fedex.aggregatorservice.controller.AggregateController;
import com.fedex.aggregatorservice.model.Aggregate;
import com.fedex.aggregatorservice.service.AggregateService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AggregateControllerTest {

    @InjectMocks
    private AggregateController aggregateController;

    @Mock
    private AggregateService aggregateService;

    @Before
    public void setup() {

        MockitoAnnotations.initMocks(this);
        this.aggregateService = mock(AggregateService.class);
        this.aggregateController = new AggregateController(aggregateService);
    }

    @Test
    public void getAggregateResponse() throws Throwable {
        //GIVEN
        List<Long> orderNumber = List.of(111111111L);
        List<String> pricing = List.of("UK", "IN", "FR");
        List<String> shipmentServiceResponse = Arrays.asList("BOX", "ENVELOPE");
        Double priceServiceResponse = 14.656565646;

        //Expected Response Object
        Aggregate aggregate = new Aggregate();
        Map<String, List<String>> map = new HashMap<>();
        map.put(orderNumber.get(0).toString(), shipmentServiceResponse);
        aggregate.setShipments(map);

        Map<String, Double> pricingMap = new HashMap<>();
        pricingMap.put(pricing.get(0).toString(), priceServiceResponse);
        aggregate.setPricing(pricingMap);

        //WHEN
        when(this.aggregateService.getAllApiAggregateDetails(orderNumber, null, pricing))
                .thenReturn(aggregate);

        ResponseEntity<Aggregate> result = this.aggregateController.getAggregateDetails(orderNumber, null, pricing);

        //THEN
        assertEquals("Compare responses ", aggregate, result.getBody());
        assertEquals("Compare responses ", aggregate.getShipments(), result.getBody().getShipments());
        assertEquals("Compare responses ", aggregate.getTrack(), result.getBody().getTrack());
        assertEquals("Compare responses ", aggregate.getPricing(), result.getBody().getPricing());
    }

    @Test
    public void getAllApiResponseForAllNullPara() throws Throwable {
        //GIVEN
        Aggregate aggregate = new Aggregate();
        Map<String, List<String>> shipmentMap = new HashMap<>();
        Map<String, List<String>> map = new HashMap<>();
        Map<String, Double> pricingMap = new HashMap<>();
        aggregate.setShipments(shipmentMap);
        aggregate.setTrack(map);
        aggregate.setPricing(pricingMap);

        //WHEN
        when(this.aggregateService.getAllApiAggregateDetails(null, null, null))
                .thenReturn(aggregate);

        ResponseEntity<Aggregate> result = this.aggregateController.getAggregateDetails(null, null, null);

        //THEN
        assertEquals("Compare responses:", aggregate, result.getBody());
    }

}
