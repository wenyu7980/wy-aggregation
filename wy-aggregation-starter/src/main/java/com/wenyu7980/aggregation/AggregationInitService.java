package com.wenyu7980.aggregation;

import com.wenyu7980.aggregation.api.service.AggregationInitInternalService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 *
 * @author wenyu
 */

@FeignClient(name = "wy-aggregation", path = "internal/aggregations", configuration = {
  FeignConfiguration.class
}, contextId = "wy-aggregation-init", fallback = AggregationInitFallbackService.class)
public interface AggregationInitService extends AggregationInitInternalService {
}
