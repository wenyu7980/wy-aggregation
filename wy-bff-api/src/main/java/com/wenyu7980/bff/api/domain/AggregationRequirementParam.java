package com.wenyu7980.bff.api.domain;

import java.util.Objects;

/**
 *
 * @author wenyu
 */
public class AggregationRequirementParam {
    /** 参数名 */
    private String name;
    /** 引用或者常量值 */
    private String value;
    /** 是否是常量 */
    private boolean constant;

    public AggregationRequirementParam() {
    }

    public AggregationRequirementParam(String name, String value, boolean constant) {
        this.name = name;
        this.value = value;
        this.constant = constant;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isConstant() {
        return constant;
    }

    public void setConstant(boolean constant) {
        this.constant = constant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AggregationRequirementParam that = (AggregationRequirementParam) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
