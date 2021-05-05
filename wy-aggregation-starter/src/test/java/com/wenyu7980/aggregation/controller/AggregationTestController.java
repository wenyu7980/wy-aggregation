package com.wenyu7980.aggregation.controller;

import com.wenyu7980.aggregation.domain.AggregationBinaryPage;
import com.wenyu7980.aggregation.domain.AggregationDetail;
import com.wenyu7980.aggregation.domain.AggregationDomain;
import com.wenyu7980.aggregation.domain.AggregationPage;
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

    @GetMapping("domains/detail/page")
    public AggregationPage<AggregationDetail> getPage() {
        return null;
    }

    @GetMapping("domains/detail/complex")
    public List<AggregationBinaryPage<List<AggregationDetail>, AggregationDomain[]>>[] complex() {
        return null;
    }

}
