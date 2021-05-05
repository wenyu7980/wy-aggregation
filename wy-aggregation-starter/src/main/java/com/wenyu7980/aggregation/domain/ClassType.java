package com.wenyu7980.aggregation.domain;

import java.util.Collection;
import java.util.HashSet;
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
    private boolean aggregatedFlag;

    /**
     * 聚合类型
     * @param name
     * @return
     */
    public static ClassType ofAggregation(String name) {
        return new ClassType(name, null, true, true);
    }

    /**
     * 普通类型
     * @param name
     * @return
     */
    public static ClassType ofCustom(String name) {
        return new ClassType(name, null, false, false);
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
        this.aggregatedFlag = attributes.stream().anyMatch(a -> a.getType().aggregatedFlag);
    }

    public void updateAggregatedFlag() {
        if (this.aggregatedFlag) {
            return;
        }
        for (ClassAttribute attribute : attributes) {
            attribute.getType().updateAggregatedFlag();
        }
        this.aggregatedFlag = attributes.stream().anyMatch(a -> a.getType().aggregatedFlag);
    }
}
