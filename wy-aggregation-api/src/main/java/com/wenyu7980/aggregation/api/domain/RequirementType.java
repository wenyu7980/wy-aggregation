package com.wenyu7980.aggregation.api.domain;

import java.util.Objects;
import java.util.Set;

/**
 *
 * @author wenyu
 */
public class RequirementType {
    private String name;
    private Set<RequirementAttribute> attributes;

    private RequirementType() {
    }

    public RequirementType(String name, Set<RequirementAttribute> attributes) {
        this.name = name;
        this.attributes = attributes;
    }

    public String getName() {
        return name;
    }

    public Set<RequirementAttribute> getAttributes() {
        return attributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RequirementType that = (RequirementType) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
