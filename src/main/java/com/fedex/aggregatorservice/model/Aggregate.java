package com.fedex.aggregatorservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Aggregate {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    HashMap<String, List<Object>> shipments;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    HashMap<String, List<Object>> track;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    HashMap<String, List<Object>> pricing;
}

