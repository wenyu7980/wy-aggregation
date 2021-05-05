package com.wenyu7980.aggregation.domain;

import java.util.List;
import java.util.Set;

/**
 *
 * @author wenyu
 */
public class AggregationBinaryPage<T, E> {
    private T[] array;
    private AggregationDetail detail;
    private List<T> list;
    private Set<E> set;
}
