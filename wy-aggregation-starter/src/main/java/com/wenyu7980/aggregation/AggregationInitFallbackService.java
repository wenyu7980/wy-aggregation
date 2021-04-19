package com.wenyu7980.aggregation;

import com.wenyu7980.aggregation.api.domain.AggregationInit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author wenyu
 */
public class AggregationInitFallbackService implements AggregationInitService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AggregationInitService.class);

    @Override
    public void aggregation(AggregationInit aggregation) {
        LOGGER.error("AggregationInitService调用失败{}", aggregation);
    }
}
