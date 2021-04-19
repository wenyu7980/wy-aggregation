package com.wenyu7980.aggregation.api.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author wenyu
 */
public class AggregationProvider {
    /** 路径格式：users/{id}，不使用通配符 */
    private String path;
    /** 类名 */
    private String className;
    /** 是否是数组 */
    private boolean arrayFlag;
    /** 参数 */
    private Set<AggregationProviderParam> params = new HashSet<>();

    public AggregationProvider() {
    }

    public AggregationProvider(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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

    public Set<AggregationProviderParam> getParams() {
        return params;
    }

    public void setParams(Set<AggregationProviderParam> params) {
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
        AggregationProvider that = (AggregationProvider) o;
        return Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }

    @Override
    public String toString() {
        return "AggregationProvider{" + "path='" + path + '\'' + ", className='" + className + '\'' + ", arrayFlag="
          + arrayFlag + ", params=" + params + '}';
    }
}
