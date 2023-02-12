package com.fedex.aggregatorservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Aggregate {
    Map<String, List<String>> shipments;

    Map<String, List<String>> track;

    Map<String, Double> pricing;
}

