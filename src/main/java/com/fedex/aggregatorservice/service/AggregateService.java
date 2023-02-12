package com.fedex.aggregatorservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fedex.aggregatorservice.model.Aggregate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class AggregateService {

    private final ShipmentService shipmentService;

    private final TrackService trackService;

    private final PricingService pricingService;

    public AggregateService(ShipmentService shipmentService, TrackService trackService, PricingService pricingService) {
        this.shipmentService = shipmentService;
        this.trackService = trackService;
        this.pricingService = pricingService;
    }

    public Aggregate getAllApiAggregateDetails(List<Long> shipmentsOrderNumbers, List<Long> trackOrderNumbers, List<String> priceCountryCodes) {

        Aggregate aggregate = new Aggregate();
        HashMap<String, List<Object>> shipmentMap = new HashMap<>();
        HashMap<String, List<Object>> trackStatusMap = new HashMap<>();
        HashMap<String, List<Object>> pricingMap = new HashMap<>();

        if(Objects.nonNull(shipmentsOrderNumbers)) {
            shipmentService.shipmentDetailsForOrders(shipmentsOrderNumbers, shipmentMap);
        }
        if(Objects.nonNull(trackOrderNumbers)) {
            trackService.trackDetails(trackOrderNumbers, trackStatusMap);
        }

        if(Objects.nonNull(priceCountryCodes)) {
            pricingService.getPricingDetailsForCountryCode(priceCountryCodes, pricingMap);
        }
        aggregate.setShipments(shipmentMap);
        aggregate.setTrack(trackStatusMap);
        aggregate.setPricing(pricingMap);
        return aggregate;
    }

}
