package com.wenyu7980.aggregation.domain;

import com.wenyu7980.aggregation.annotation.Aggregation;
import com.wenyu7980.aggregation.annotation.AggregationParam;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author wenyu
 */
public class AggregationDetail {
    private AggregationDomain domain;
    private Set<AggregationDomain> set;
    /** 不支持递归，递归会报栈溢出异常 */
    // private List<AggregationDetail> deeps;
    /** 不支持map */
    private Map<String, AggregationDomain> map;
    @Aggregation(params = { @AggregationParam(name = "id", value = "1", constant = true) })
    private List<AggregationDomain> list;
}
