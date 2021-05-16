package com.wenyu7980.aggregation.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author wenyu
 */
public class ClassType {
    private final String name;
    private Set<ClassAttribute> attributes;
    /** 是否是聚合类 */
    private final boolean aggregationFlag;
    private Boolean aggregatedFlag;

    /**
     * 聚合类型
     * @param name
     * @return
     */
    public static ClassType ofAggregation(String name) {
        return new ClassType(name, new HashSet<>(), true, true);
    }

    /**
     * 普通类型
     * @param name
     * @return
     */
    public static ClassType ofCustom(String name) {
        return new ClassType(name, new HashSet<>(), false, false);
    }

    private ClassType(String name, Set<ClassAttribute> attributes, boolean aggregationFlag, boolean aggregatedFlag) {
        this.name = name;
        this.attributes = attributes;
        this.aggregationFlag = aggregationFlag;
        this.aggregatedFlag = aggregatedFlag;
    }

    public String getName() {
        return name;
    }

    public Set<ClassAttribute> getAttributes() {
        return attributes;
    }

    public boolean isAggregationFlag() {
        return aggregationFlag;
    }

    public boolean isAggregatedFlag() {
        return aggregatedFlag;
    }

    public void setAttributes(Collection<ClassAttribute> attributes) {
        this.attributes = new HashSet<>(attributes);
    }

    public void updateAggregatedFlag() {
        if (this.aggregatedFlag != null) {
            return;
        }
        for (ClassAttribute attribute : attributes) {
            attribute.getType().updateAggregatedFlag();
        }
        this.aggregatedFlag = attributes.stream().anyMatch(a -> a.getType().aggregatedFlag);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClassType classType = (ClassType) o;
        return Objects.equals(name, classType.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
