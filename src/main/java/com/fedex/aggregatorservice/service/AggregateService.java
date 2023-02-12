package com.fedex.aggregatorservice.service;

import com.fedex.aggregatorservice.model.Aggregate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

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
        Map<String, List<String>> shipmentMap = new HashMap<>();
        Map<String, List<String>> trackStatusMap = new HashMap<>();
        Map<String, Double> pricingMap = new HashMap<>();

        if (Objects.nonNull(shipmentsOrderNumbers)) {
            shipmentService.shipmentDetailsForOrders(shipmentsOrderNumbers, shipmentMap);
        }
        if (Objects.nonNull(trackOrderNumbers)) {
            trackService.trackDetails(trackOrderNumbers, trackStatusMap);
        }

        if (Objects.nonNull(priceCountryCodes)) {
            pricingService.getPricingDetailsForCountryCode(priceCountryCodes, pricingMap);
        }
        aggregate.setShipments(shipmentMap);
        aggregate.setTrack(trackStatusMap);
        aggregate.setPricing(pricingMap);
        return aggregate;
    }

}
