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

@Service
@Slf4j
public class AggregateService {

    HttpClient client = HttpClient.newHttpClient();
    @Value("${service.url}")
    private String serviceUrl;

    @Value("${service.timeout}")
    private Long serviceTimeout;

    public Aggregate getAllApiDetails(List<Long> shipmentOrderNumber, List<Long> trackOrderNumber, List<String> priceCountryCode) {

        Aggregate aggregate = new Aggregate();
        HashMap<String, List<Object>> shipmentMap = new HashMap<>();
        HashMap<String, List<Object>> trackStatusMap = new HashMap<>();
        HashMap<String, List<Object>> pricingMap = new HashMap<>();

        if(Objects.nonNull(shipmentOrderNumber)) {
            shipmentOrderNumber.forEach(order -> {
                CompletableFuture<String> shipmentCompletableFuture = null;
                try {
                    shipmentCompletableFuture = getShipmentOrderDetails(order);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                String s = shipmentCompletableFuture.getNow(null);
                if (Objects.nonNull(s)) {
                    s = s.replaceAll("\"", "");
                    shipmentMap.put(String.valueOf(order), Arrays.asList(s.split("/,/")));
                }
            });
        }
        if(Objects.nonNull(trackOrderNumber)) {
            trackOrderNumber.forEach(order -> {
                CompletableFuture<String> trackCompletableFuture = null;
                try {
                    trackCompletableFuture = getTrackStatusDetails(order);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                String s = trackCompletableFuture.getNow(null);
                if (Objects.nonNull(s)) {
                    s = s.replaceAll("\"", "");
                    trackStatusMap.put(String.valueOf(order), Arrays.asList(s.split("/,/")));
                }
            });
        }

        if(Objects.nonNull(priceCountryCode)) {
            priceCountryCode.forEach(countryCode -> {
                CompletableFuture<String> pricingCompletableFuture = null;
                try {
                    pricingCompletableFuture = getPricingDetails(countryCode);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                String s = pricingCompletableFuture.getNow(null);
                if (Objects.nonNull(s)) {
                    s = s.replaceAll("\"", "");
                    pricingMap.put(String.valueOf(countryCode), Arrays.asList(s.split("/,/")));
                }
            });
        }
        aggregate.setShipments(shipmentMap);
        aggregate.setTrack(trackStatusMap);
        aggregate.setPricing(pricingMap);
        return aggregate;
    }

    @Async
    public CompletableFuture<String> getShipmentOrderDetails(Long shipmentOrderNumber) throws JsonProcessingException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:4000/shipment-products?orderNumber=" + shipmentOrderNumber))
                .GET()
                .header("Accept", "application/json")
                .timeout(Duration.ofMillis(500))
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            log.error("Time out error !!!");
        }

        if(Objects.isNull(response)) {
            return CompletableFuture.completedFuture(null);
        }
        log.debug("status of shipment service " + response.statusCode());

        return CompletableFuture.completedFuture(response.body());
    }

    @Async
    public CompletableFuture<String> getTrackStatusDetails(Long trackOrderNumber) throws JsonProcessingException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:4000/track-status?orderNumber=" + trackOrderNumber))
                .GET()
                .header("Accept", "application/json")
                .timeout(Duration.ofMillis(500))
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            log.error("Time out error !!!");
        }
        if(Objects.isNull(response)) {
            return CompletableFuture.completedFuture(null);
        }
        log.debug("status of tracking service " + response.statusCode());

        return CompletableFuture.completedFuture(response.body());
    }

    @Async
    public CompletableFuture<String> getPricingDetails(String countryCode) throws JsonProcessingException {

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://127.0.0.1:4000/pricing?countryCode=" + countryCode))
            .GET()
            .header("Accept", "application/json")
            .timeout(Duration.ofMillis(500))
            .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            log.error("Time out error !!!");
        }
        if(Objects.isNull(response)) {
            return CompletableFuture.completedFuture(null);
        }
        log.debug("status of pricing service " + response.statusCode());

        return CompletableFuture.completedFuture(response.body());
    }

}
