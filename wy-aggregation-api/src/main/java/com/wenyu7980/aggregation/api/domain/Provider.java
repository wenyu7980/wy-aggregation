package com.wenyu7980.aggregation.api.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author wenyu
 */
public class Provider {
    /** 路径格式：users/{id}，不使用通配符 */
    private String path;
    /** 类名 */
    private String typeName;
    /** 参数 */
    private Set<ProviderParam> params = new HashSet<>();

    private Provider() {
    }

    public Provider(String path, String className, Set<ProviderParam> params) {
        this.path = path;
        this.typeName = className;
        this.params = params;
    }

    public String getPath() {
        return path;
    }

    public String getTypeName() {
        return typeName;
    }

    public Set<ProviderParam> getParams() {
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
        Provider that = (Provider) o;
        return Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }

    @Override
    public String toString() {
        return "AggregationProvider{" + "path='" + path + '\'' + ", className='" + typeName + '\'' + ", params="
          + params + '}';
    }
}
