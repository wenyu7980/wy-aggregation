package com.wenyu7980.aggregation.controller;

import com.wenyu7980.aggregation.annotation.Aggregation;
import com.wenyu7980.aggregation.domain.AggregationDetail;
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
public class AggregationTestController {


    @GetMapping("domains/detail/{id}")
    public AggregationDetail getDetail(@PathVariable("id") String id) {
        return null;
    }

    @GetMapping("domains/detail/List")
    public List<AggregationDetail> getDetailList() {
        return null;
    }

}
