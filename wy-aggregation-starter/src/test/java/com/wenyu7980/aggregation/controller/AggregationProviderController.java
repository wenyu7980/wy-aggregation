package com.wenyu7980.aggregation.controller;

import com.wenyu7980.aggregation.annotation.Aggregation;
import com.wenyu7980.aggregation.domain.AggregationDomain;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author wenyu
 */
@RestController
@RequestMapping
public class AggregationProviderController {
    @Aggregation
    @GetMapping("aggregation/domains/{id}")
    public AggregationDomain get(@PathVariable("id") String id) {
        return new AggregationDomain();
    }
}
