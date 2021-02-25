package com.wenyu7980.bff.api.domain;

import java.util.Objects;

/**
 *
 * @author wenyu
 */
public class AggregationProviderParam {
    /** 参数名 */
    private String name;
    /** 是否是PathVariable */
    private boolean pathFlag;

    public AggregationProviderParam() {
    }

    public AggregationProviderParam(String name, boolean pathFlag) {
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
        AggregationProviderParam that = (AggregationProviderParam) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
