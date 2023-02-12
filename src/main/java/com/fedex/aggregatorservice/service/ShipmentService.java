package com.fedex.aggregatorservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fedex.aggregatorservice.configuration.ServiceConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
@Service
@Slf4j
public class ShipmentService {

    private final ServiceConfiguration serviceConfiguration;
    HttpClient client = HttpClient.newHttpClient();

    public ShipmentService(ServiceConfiguration serviceConfiguration) {
        this.serviceConfiguration = serviceConfiguration;
    }

    @Async
    public CompletableFuture<String> callExternalShipmentService(Long shipmentOrderNumber) throws JsonProcessingException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serviceConfiguration.getBaseUrl() + "shipment-products?orderNumber=" + shipmentOrderNumber))
                .GET()
                .header("Accept", "application/json")
                .timeout(Duration.ofMillis(serviceConfiguration.getTimeout()))
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            log.error("Time out error !!!");
        }
        if (Objects.isNull(response)) {
            return CompletableFuture.completedFuture("");
        }
        return CompletableFuture.completedFuture(response.body());
    }


    public void shipmentDetailsForOrders(List<Long> shipmentsOrderNumbers, Map<String, List<String>> shipmentMap) {
        shipmentsOrderNumbers.forEach(order -> {
            String shipmentResponse = null;
            try {
                shipmentResponse = callExternalShipmentService(order).getNow(null);
                if (Objects.nonNull(shipmentResponse) && !shipmentResponse.isEmpty()
                        && !shipmentResponse.equalsIgnoreCase("{message:service unavailable}")) {
                    shipmentResponse = shipmentResponse.replaceAll("\"", "");
                    shipmentMap.put(String.valueOf(order), Arrays.asList(shipmentResponse.split("/,/")));
                }
            } catch (JsonProcessingException e) {
                log.error("shipment service for order number {} is failing for {}", order, e.getMessage());
            }
        });
    }

}
