package com.wenyu7980.bff.api.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author wenyu
 */
public class AggregationRequirementAttribute {
    /** 属性名,例如：data.user */
    private String attribute;
    /** 类名 */
    private String className;
    /** 是否是数组 */
    private boolean arrayFlag;
    /** 查询参数 */
    private Set<AggregationRequirementParam> params = new HashSet<>();

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public boolean isArrayFlag() {
        return arrayFlag;
    }

    public void setArrayFlag(boolean arrayFlag) {
        this.arrayFlag = arrayFlag;
    }

    public Set<AggregationRequirementParam> getParams() {
        return params;
    }

    public void setParams(Set<AggregationRequirementParam> params) {
        this.params = params;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AggregationRequirementAttribute that = (AggregationRequirementAttribute) o;
        return Objects.equals(attribute, that.attribute);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attribute);
    }
}
