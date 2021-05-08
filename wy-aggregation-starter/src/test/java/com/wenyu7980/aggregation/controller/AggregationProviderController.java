package com.wenyu7980.aggregation.controller;

import com.wenyu7980.aggregation.annotation.AggregationMethod;
import com.wenyu7980.aggregation.domain.AggregationDomain;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * @author wenyu
 */
@RestController
@RequestMapping
public class AggregationProviderController {
    @AggregationMethod
    @GetMapping("aggregation/domains/{id}")
    public AggregationDomain get(@PathVariable("id") String id) {
        return new AggregationDomain();
    }

    @AggregationMethod
    @GetMapping("aggregation/domains/list")
    public List<AggregationDomain> get() {
        return null;
    }
}
