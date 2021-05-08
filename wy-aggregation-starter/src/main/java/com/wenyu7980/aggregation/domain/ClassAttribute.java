package com.wenyu7980.aggregation.domain;

import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author wenyu
 */
public class ClassAttribute {
    private final String name;
    private final ClassType type;
    private final List<AggregationParam> params;

    public static ClassAttribute ofAggregation(String name, ClassType type, List<AggregationParam> params) {
        Assert.isTrue(type.isAggregationFlag(), "聚合数组的type的AggregationFlag必须为true");
        return new ClassAttribute(name, type, params);
    }

    public static ClassAttribute ofCustom(String name, ClassType type) {
        return new ClassAttribute(name, type, new ArrayList<>());
    }

    private ClassAttribute(String name, ClassType type, List<AggregationParam> params) {
        this.name = name;
        this.type = type;
        this.params = params;
    }

    public String getName() {
        return name;
    }

    public ClassType getType() {
        return type;
    }

    public List<AggregationParam> getParams() {
        return params;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClassAttribute that = (ClassAttribute) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}

