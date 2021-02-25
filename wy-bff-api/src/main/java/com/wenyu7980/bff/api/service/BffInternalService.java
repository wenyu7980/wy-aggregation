package com.wenyu7980.bff.api.service;

import com.wenyu7980.bff.api.domain.AggregationInit;
import com.wenyu7980.bff.api.domain.AggregationItem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 *
 * @author wenyu
 */
@FeignClient(name = "wy-bff", path = "internal/aggregations", contextId = "wy-bff-aggregation")
public interface BffInternalService {
    /**
     * 初始化
     * @param aggregation
     */
    @PostMapping()
    void provider(@RequestBody AggregationInit aggregation);

    @GetMapping()
    List<AggregationItem> getItem();
}
