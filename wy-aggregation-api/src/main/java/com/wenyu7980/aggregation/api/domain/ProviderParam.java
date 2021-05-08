package com.wenyu7980.aggregation.api.domain;

import java.util.Objects;

/**
 *
 * @author wenyu
 */
public class ProviderParam {
    /** 参数名 */
    private String name;
    /** 是否是PathVariable */
    private boolean pathFlag;

    public ProviderParam() {
    }

    public static ProviderParam ofPath(String name) {
        return new ProviderParam(name, true);
    }

    public static ProviderParam ofQuery(String name) {
        return new ProviderParam(name, true);
    }

    private ProviderParam(String name, boolean pathFlag) {
        this.name = name;
        this.pathFlag = pathFlag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPathFlag() {
        return pathFlag;
    }

    public void setPathFlag(boolean pathFlag) {
        this.pathFlag = pathFlag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProviderParam that = (ProviderParam) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "AggregationProviderParam{" + "name='" + name + '\'' + ", pathFlag=" + pathFlag + '}';
    }
}
