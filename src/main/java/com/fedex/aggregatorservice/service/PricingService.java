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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;


@Service
@Slf4j
public class PricingService {
    HttpClient client = HttpClient.newHttpClient();
    private final ServiceConfiguration serviceConfiguration;

    public PricingService(ServiceConfiguration serviceConfiguration) {
        this.serviceConfiguration = serviceConfiguration;
    }

    @Async
    public CompletableFuture<String> getPricingDetails(String countryCode) throws JsonProcessingException {

        HttpResponse<String> response = null;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serviceConfiguration.getBaseUrl() + "pricing?countryCode=" + countryCode))
                .GET()
                .header("Accept", "application/json")
                .timeout(Duration.ofMillis(serviceConfiguration.getTimeout()))
                .build();
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

    public void getPricingDetailsForCountryCode(List<String> priceCountryCodes, Map<String, Double> pricingMap) {
        priceCountryCodes.forEach(countryCode -> {
            String pricingDetails = null;
            try {
                pricingDetails = getPricingDetails(countryCode).getNow(null);
            } catch (JsonProcessingException e) {
                log.error("pricing service for countrycode {} is failing for{}", countryCode, e.getMessage());
            }
            if (Objects.nonNull(pricingDetails)
                    && !pricingDetails.isEmpty()
                    && !pricingDetails.equalsIgnoreCase("{message:service unavailable}")) {
                pricingDetails = pricingDetails.replaceAll("\"", "");
                Double pricingDetail = Double.parseDouble(pricingDetails);
                pricingMap.put(String.valueOf(countryCode), pricingDetail);
            }
        });
    }
}
