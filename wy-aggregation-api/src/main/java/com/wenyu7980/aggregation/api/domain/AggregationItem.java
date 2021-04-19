package com.wenyu7980.aggregation.api.domain;

import java.util.Objects;

/**
 *
 * @author wenyu
 */
public class AggregationItem {
    private String serviceName;
    private String method;
    private String path;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AggregationItem that = (AggregationItem) o;
        return Objects.equals(serviceName, that.serviceName) && Objects.equals(method, that.method) && Objects
          .equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceName, method, path);
    }

    @Override
    public String toString() {
        return "AggregationItem{" + "serviceName='" + serviceName + '\'' + ", method='" + method + '\'' + ", path='"
          + path + '\'' + '}';
    }
}
