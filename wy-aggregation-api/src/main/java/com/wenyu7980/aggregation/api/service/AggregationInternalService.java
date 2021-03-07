package com.wenyu7980.aggregation.api.service;

import com.wenyu7980.aggregation.api.domain.AggregationInit;
import com.wenyu7980.aggregation.api.domain.AggregationItem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Set;

/**
 *
 * @author wenyu
 */
@FeignClient(name = "wy-aggregation", path = "internal/aggregations", contextId = "wy-aggregation")
public interface AggregationInternalService {
    /**
     * 初始化
     * @param aggregation
     */
    @PostMapping()
    void aggregation(@RequestBody AggregationInit aggregation);

    /**
     * 获取需要聚合的接口
     * @return
     */
    @GetMapping()
    Set<AggregationItem> getItem();
}
