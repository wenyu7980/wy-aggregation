package com.wenyu7980.aggregation.domain;

/**
 *
 * @author wenyu
 */
public class AggregationParam {
    private final String name;
    private final String value;
    private final boolean constant;

    public AggregationParam(String name, String value, boolean constant) {
        this.name = name;
        this.value = value;
        this.constant = constant;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public boolean isConstant() {
        return constant;
    }
}
