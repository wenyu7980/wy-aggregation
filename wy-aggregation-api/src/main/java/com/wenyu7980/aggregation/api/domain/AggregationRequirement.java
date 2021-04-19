package com.wenyu7980.aggregation.api.domain;

import java.util.Objects;
import java.util.Set;

/**
 *
 * @author wenyu
 */
public class AggregationRequirement {
    /** 方法 */
    private String method;
    /** 路径格式：users/*，使用通配符 */
    private String path;
    /** 属性 */
    private Set<AggregationRequirementAttribute> attributes;

    public AggregationRequirement() {
    }

    public AggregationRequirement(String method, String path, Set<AggregationRequirementAttribute> attributes) {
        this.method = method;
        this.path = path;
        this.attributes = attributes;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Set<AggregationRequirementAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(Set<AggregationRequirementAttribute> attributes) {
        this.attributes = attributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AggregationRequirement that = (AggregationRequirement) o;
        return Objects.equals(method, that.method) && Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, path);
    }

    @Override
    public String toString() {
        return "AggregationRequirement{" + "method='" + method + '\'' + ", path='" + path + '\'' + ", attributes="
          + attributes + '}';
    }
}
