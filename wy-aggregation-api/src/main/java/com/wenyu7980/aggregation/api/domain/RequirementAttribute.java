package com.wenyu7980.aggregation.api.domain;

import java.util.Objects;
import java.util.Set;

/**
 *
 * @author wenyu
 */
public class RequirementAttribute {
    private String name;
    private String type;
    private Set<RequirementParam> params;

    private RequirementAttribute() {
    }

    public RequirementAttribute(String name, String type, Set<RequirementParam> params) {
        this.name = name;
        this.type = type;
        this.params = params;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Set<RequirementParam> getParams() {
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
        RequirementAttribute that = (RequirementAttribute) o;
        return Objects.equals(name, that.name) && Objects.equals(type, that.type) && Objects.equals(params, that.params);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
