package com.fedex.aggregatorservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Aggregate {
    Map<String, List<Object>> shipments;

    Map<String, List<Object>> track;

    Map<String, List<Object>> pricing;
}

