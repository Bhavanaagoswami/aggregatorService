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
import java.util.stream.Collectors;

@Service
@Slf4j
public class TrackService {
    HttpClient client = HttpClient.newHttpClient();
    private final ServiceConfiguration serviceConfiguration;

    public TrackService(ServiceConfiguration serviceConfiguration) {
        this.serviceConfiguration = serviceConfiguration;
    }

    @Async
    public CompletableFuture<String> getTrackStatusDetails(Long trackOrderNumber) throws JsonProcessingException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serviceConfiguration.getBaseUrl()+"track-status?orderNumber=" + trackOrderNumber))
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
        if(Objects.isNull(response)) {
            return CompletableFuture.completedFuture("");
        }
        return CompletableFuture.completedFuture(response.body());
    }

    public void trackDetails(List<Long> trackOrderNumbers, Map<String, List<String>> trackStatusMap) {
        trackOrderNumbers.forEach(order -> {
            String trackResponse = null;
            try {
                trackResponse = getTrackStatusDetails(order).getNow(null);
            } catch (JsonProcessingException e) {
                log.error("track service for order number {} is failing for {}",order,e.getMessage());
            }
            if (Objects.nonNull(trackResponse) && !trackResponse.isEmpty() && !trackResponse.equalsIgnoreCase("{message:service unavailable}")) {
                trackResponse = trackResponse.replaceAll("\"", "");
                List<String> trackResponseList = Arrays.stream(trackResponse.split("/,/")).collect(Collectors.toList());
                trackStatusMap.put(String.valueOf(order), trackResponseList);
            }
        });
    }
}
