package com.fedex.aggregatorservice.controller;

import com.fedex.aggregatorservice.exception.ApiTimeoutException;
import com.fedex.aggregatorservice.exception.ErrorResponse;
import com.fedex.aggregatorservice.model.Aggregate;
import com.fedex.aggregatorservice.service.AggregateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
public class AggregateController {
    private final AggregateService aggregateService;

    public AggregateController(AggregateService aggregateService) {
        this.aggregateService = aggregateService;
    }

    @GetMapping(value = "/aggregation", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Aggregate> getAggregateDetails(@RequestParam(required = false) List<Long> shipmentsOrderNumbers,
                                                         @RequestParam(required = false) List<Long> trackOrderNumbers,
                                                         @RequestParam(required = false) List<String> pricingCountryCodes) {
        Aggregate aggregate = aggregateService.getAllApiAggregateDetails(shipmentsOrderNumbers, trackOrderNumbers, pricingCountryCodes);
        return ResponseEntity.ok(aggregate);
    }

    @ExceptionHandler(value = ApiTimeoutException.class)
    @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
    public ErrorResponse handleApiTimeoutException(ApiTimeoutException ex) {
        return new ErrorResponse(HttpStatus.REQUEST_TIMEOUT.value(), ex.getMessage());
    }
}
