package com.wenyu7980.aggregation.domain;

import java.util.List;

/**
 *
 * @author wenyu
 */
public class AggregationPage<T> {
    private List<T> data;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
